package scdy.configservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import scdy.configservice.Exception.FileSizeOverException;
import scdy.configservice.Exception.IllegalFileException;
import scdy.configservice.Exception.LackOfImageInfoException;
import scdy.configservice.dto.ImageRequestDto;
import scdy.configservice.enums.ImageFormat;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;


@Slf4j
@RequiredArgsConstructor
@Service
public class ImageValidationService {

//    private static final Map<String, byte[]> IMAGE_MAGIC_NUMBERS = Map.of(
//            "jpeg", new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) (0xFF)}, // JPEG
//            "png", new byte[]{(byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47, (byte) 0x0D, (byte) 0x0A, (byte) 0x1A, (byte) 0x0A}, // PNG
//            "gif", new byte[]{(byte) 0x47, (byte) 0x49, (byte) 0x46, (byte) 0x38} // GIF
//    );

    @Value("${imageSize.MAX_FILE_SIZE}")
    private Long MAX_FILE_SIZE;

    //이미지 검증
    //용량 제한 5MB
    public void validationImage(MultipartFile image, ImageRequestDto imageRequestDto) {

        //빈 파일인 경우 예외
        if(image.isEmpty() || Objects.isNull(image.getOriginalFilename())){
            throw new IllegalFileException("빈 파일입니다.");
        }

        validationImageSize(image);
        validateImageType(imageRequestDto); //
        validateImageFileExtension(image); //확장자 및 MIME 검증
        validationImageMagicNumber(image); // 매직넘버 검증
    }

    //이미지 용량 검증
    private void validationImageSize(MultipartFile image){

        if (image.getSize() > MAX_FILE_SIZE) {
            throw new FileSizeOverException("파일 크기가 5MB를 초과합니다.");
        }
    }


    //이미지 타입 검증
    private void validateImageType(ImageRequestDto imageRequestDto) {

        switch (imageRequestDto.getImageType()) {

            case USER:
                if (imageRequestDto.getUserId() == null) {
                    throw new LackOfImageInfoException("사용자 정보(ID)가 없습니다.");
                }
                break; // 해당 case 처리 후 switch 문을 빠져나갑니다.

            case BOARD:
                if (imageRequestDto.getBoardId() == null) {
                    throw new LackOfImageInfoException("게시글 정보(ID)가 부족합니다.");
                }
                break;

            case CONTENTS:
                if (imageRequestDto.getContentsId() == null) {
                    throw new LackOfImageInfoException("콘텐츠 정보(ID)가 부족합니다.");
                }
                break;
            default:
                throw new IllegalFileException("알 수 없는 이미지 타입입니다.");
        }
    }

    //이미지 확장자 및 MIME 검증
    //"jpg", "jpeg", "png", "gif" 만 지원
    private void validateImageFileExtension(MultipartFile image) {
        String originalFilename = image.getOriginalFilename();
        String contentType = image.getContentType();

        // 확장자 검증
        int lastDotIndex = originalFilename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new IllegalFileException("잘못된 파일입니다(확장자 없음).");
        }
        String extension = originalFilename.substring(lastDotIndex + 1).toLowerCase();

        // ImageFormat Enum을 사용하여 지원하는 확장자인지 확인
        ImageFormat imageFormatFromExtension = ImageFormat.fromExtension(extension);
        if (imageFormatFromExtension == null) {
            throw new IllegalFileException("지원하지 않는 형식입니다: " + extension);
        }

        // MIME 검증
        if (contentType == null || !imageFormatFromExtension.getMimeType().equalsIgnoreCase(contentType)) {
            throw new IllegalFileException("파일 확장자와 실제 MIME 타입이 일치하지 않거나 지원하지 않는 MIME 타입입니다. " +
                    "확장자: " + extension + ", 실제 MIME: " + contentType);
        }
    }

    //이미지 매직 넘버 검증
    private void validationImageMagicNumber(MultipartFile imageFile) {
        if (imageFile.isEmpty()) {
            throw new IllegalFileException("업로드된 파일이 비어 있습니다.");
        }

        try (InputStream is = imageFile.getInputStream()) {
            // 모든 지원하는 매직 넘버의 최대 길이를 고려하여 충분히 읽기
            // 현재 PNG가 8바이트로 가장 길므로 8바이트 읽음
            byte[] header = new byte[8];
            int bytesRead = is.read(header);

            if (bytesRead < 4) { // 최소한 4바이트는 읽어야 일반적인 이미지 형식 판별 가능 (GIF)
                throw new IllegalFileException("파일 헤더를 읽을 수 없습니다.");
            }

            boolean isImage = false;
            String detectedType = "Unknown";

            // ImageFormat Enum을 순회하며 매직 넘버 일치 여부 확인
            for (ImageFormat format : ImageFormat.values()) {
                byte[] magic = format.getMagicNumber();
                if (bytesRead >= magic.length && Arrays.equals(Arrays.copyOfRange(header, 0, magic.length), magic)) {
                    isImage = true;
                    detectedType = format.name(); // Enum 이름 사용
                    break;
                }
            }

            if (!isImage) {
                throw new IllegalFileException("유효한 이미지 파일이 아닙니다 (매직 넘버 불일치). 감지된 타입: " + detectedType);
            }

        } catch (IOException e) {
            log.error("파일을 읽는 중 오류가 발생했습니다.", e); // 예외 객체 직접 전달
            throw new IllegalFileException("파일을 읽는 중 오류가 발생했습니다.");
        }
    }


    public String sanitizeFilename(String filename) {
        if (filename == null) return "unknown";

        // 경로 순회 문자 제거 및 안전한 문자만 유지
        return filename.replaceAll("[^a-zA-Z0-9._-]", "_")
                .replaceAll("^\\.+", "") // 시작 점 제거
                .substring(0, Math.min(filename.length(), 100)); // 길이 제한
    }
}
