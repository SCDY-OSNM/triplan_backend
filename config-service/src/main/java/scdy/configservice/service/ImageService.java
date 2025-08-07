package scdy.configservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import scdy.configservice.Exception.S3Exception;
import scdy.configservice.Exception.S3keyDuplicatedException;
import scdy.configservice.dto.ImageRequestDto;
import scdy.configservice.dto.ImageResponseDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {

    private final S3ImageService s3ImageService;
    private final ImageValidationService imageValidationService;
    private final ImageInfoService imageInfoService;

    //S3Key 중복 시 재시도 로직 추가(3회 재시도)
    private static final int MAX_RETRY_COUNT = 3;


    //이미지 저장 로직
    public ImageResponseDto upload(MultipartFile image, ImageRequestDto imageRequestDto) {

        //이미지 검증
        imageValidationService.validationImage(image, imageRequestDto);

        String s3Key = generateUniqueS3Key(image, imageRequestDto);

        //S3 업로드
        String s3Url = uploadToS3WithRetry(image, s3Key);

        //이미지 정보 업로드
        return imageInfoService.saveImageInfo(image, imageRequestDto, s3Url);
    }


    //유니크한 S3 Key 생성 (재시도 로직 포함)
    private String generateUniqueS3Key(MultipartFile image, ImageRequestDto imageRequestDto) {

        int attempt = 0;

        while (attempt < MAX_RETRY_COUNT) {
            attempt++;
            String s3Key = createS3KeyWithUUID(image, imageRequestDto);

            // S3에 동일한 키가 존재하는지 확인
            if (!s3ImageService.doesImageExist(s3Key)) {
                log.debug("유니크한 S3 Key 생성 성공 - 시도: {}/{}, Key: {}", attempt, MAX_RETRY_COUNT, s3Key);
                return s3Key;
            }

            log.warn("S3 Key 중복 발생 - 시도: {}/{}, Key: {}",
                    attempt, MAX_RETRY_COUNT, s3Key);
        }

        throw new S3keyDuplicatedException(
                String.format("S3 Key 생성 실패 - %d회 시도 후에도 중복된 키가 생성됩니다.", MAX_RETRY_COUNT)
        );
    }


    //S3 key 생성
    private String createS3KeyWithUUID(MultipartFile image, ImageRequestDto imageRequestDto) {

        String extension = extractFileExtension(image.getOriginalFilename());


        //경로 포함으로 타입별 이미지 경로 분리
        //업로드 파일 명: YYMMDD-HHmmssSSS_원본파일명.확장자
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd-HHmmssSSS");
        String timestamp = now.format(formatter);

        String shortUuid = UUID.randomUUID().toString().substring(0, 8);

        return String.format("%s/%s_%s.%s",
                imageRequestDto.getImageType().toString().toLowerCase(),
                timestamp,
                shortUuid,
                extension
        );

    }

    //파일 확장자 추출
    private String extractFileExtension(String filename) {

        if (filename == null || !filename.contains(".")) {
            return "bin"; // 기본 확장자
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    //파일 업로드 실행
    private String uploadToS3WithRetry(MultipartFile image, String s3Key) {

        int attempt = 0;
        S3Exception lastException = null;

        while (attempt < MAX_RETRY_COUNT) {
            attempt++;
            try {
                String s3Url = s3ImageService.upload(image, s3Key);
                log.info("S3 업로드 성공 - 시도: {}/{}, 파일: {}, Key: {}",
                        attempt, MAX_RETRY_COUNT, image.getOriginalFilename(), s3Key);
                return s3Url;

            } catch (S3Exception e) {

                lastException = e;
                log.warn("S3 업로드 실패 - 시도: {}/{}, 파일: {}, Key: {}: {}",
                        attempt, MAX_RETRY_COUNT, image.getOriginalFilename(), s3Key, e.getMessage());

                // 마지막 시도가 아닌 경우에만 대기
                if (attempt < MAX_RETRY_COUNT) {

                    try {
                        Thread.sleep(100L * attempt); // 점진적 백오프
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new S3Exception("S3 업로드 재시도 중 인터럽트 발생");
                    }
                }
            }
        }

        // 모든 재시도 실패 시
        throw new S3Exception(
                String.format("S3 업로드 실패 - %d회 시도 후 실패: %s", MAX_RETRY_COUNT, lastException.getMessage())
        );
    }

    //이미지 삭제 시 정합성(DB 먼저 삭제 후 s3에서 삭제.)


    //(S3 삭제 실패 시 주기적 검사로 처리)

}
