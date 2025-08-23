package scdy.configservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import scdy.configservice.Exception.ImageNotFoundException;
import scdy.configservice.Exception.S3Exception;
import scdy.configservice.Exception.S3keyDuplicatedException;
import scdy.configservice.dto.ImageRequestDto;
import scdy.configservice.dto.ImageResponseDto;
import scdy.configservice.entity.Image;
import scdy.configservice.repository.ImageRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;
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
        return imageInfoService.saveImageInfo(image, imageRequestDto, s3Url, s3Key);
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


    //사용자 프로필 이미지 조회
    @Transactional(readOnly = true)
    public ImageResponseDto getUserProfile(Long userId){

        Image image = imageRepository.findByUserId(userId).orElseThrow(
                () -> new ImageNotFoundException("존재하지 않는 이미지입니다."));

        return ImageResponseDto.from(image);
    }

    // 이미지 엔티티 리스트를 DTO 리스트로 변환하는 공통 로직
    private List<ImageResponseDto> toImageResponseDtoList(List<Image> imageList) {

        return imageList.stream()
                .map(ImageResponseDto::from)
                .toList();
    }

    // 게시글 별 이미지 목록 조회
    @Transactional(readOnly = true)
    public List<ImageResponseDto> getImagesByBoardId(Long boardId) {

        List<Image> imageList = imageRepository.findAllByBoardId(boardId);

        return toImageResponseDtoList(imageList);
    }

    // 콘텐츠 별 이미지 목록 조회
    @Transactional(readOnly = true)
    public List<ImageResponseDto> getImagesByContentsId(Long contentsId) {

        List<Image> imageList = imageRepository.findAllByContentsId(contentsId);

        return toImageResponseDtoList(imageList);
    }


//이미지 삭제
    //이미지 삭제 시 정합성(DB 먼저 삭제 후 s3에서 삭제.)
    //단일 이미지 삭제
    public void deleteImageById(Long imageId) {

        Image image = imageRepository.findById(imageId).orElseThrow(() -> new ImageNotFoundException("존재하지 않는 이미지"));
        String s3Key = image.getS3Key();

        //DB에서 이미지 삭제
        imageInfoService.deleteImageInfo(imageId);
        log.info("DB에서 이미지 정보 삭제 완료. s3Key: {}", s3Key);

        //S3에서 이미지 삭제
        try{
            s3ImageService.deleteImageFromS3(s3Key);
            log.info("S3에서 이미지 파일 삭제 성공. s3Key: {}", s3Key);
        }catch (Exception e){
            s3ImageService.saveDeleteErrorLog(s3Key);
        }
    }


    //복수 이미지 삭제(게시글 및 콘텐츠)

    // 게시글 ID로 다중 이미지 삭제
    @Transactional
    public void deleteImagesByBoardId(Long boardId) {

        List<Image> imagesToDelete = imageRepository.findAllByBoardId(boardId);

        if (imagesToDelete.isEmpty()) {
            log.info("게시글 ID {}에 연결된 이미지가 없어 삭제를 건너뜁니다.", boardId);
            return;
        }

        deleteImagesAndHandleS3(imagesToDelete, "게시글", boardId);
    }

    // 콘텐츠 ID로 다중 이미지 삭제
    @Transactional
    public void deleteImagesByContentsId(Long contentsId) {

        List<Image> imagesToDelete = imageRepository.findAllByContentsId(contentsId);

        if (imagesToDelete.isEmpty()) {
            log.info("콘텐츠 ID {}에 연결된 이미지가 없어 삭제를 건너뜁니다.", contentsId);
            return;
        }

        deleteImagesAndHandleS3(imagesToDelete, "콘텐츠", contentsId);
    }

    private void deleteImagesAndHandleS3(List<Image> images, String type, Long id) {

        //이미지 정보 삭제
        List<String> s3Keys = images.stream()
                .map(Image::getS3Key)
                .toList();

        imageRepository.deleteAllInBatch(images);
        log.info("DB에서 {} ID {}의 이미지 {}개 정보 삭제 완료.", type, id, images.size());

        // S3에서 이미지 파일 삭제
        for (String s3Key : s3Keys) {
            try {
                s3ImageService.deleteImageFromS3(s3Key);
                log.info("S3에서 이미지 파일 삭제 성공. s3Key: {}", s3Key);
            } catch (Exception e) {
                log.error("S3에서 이미지 파일 삭제 실패. s3Key: {}: {}", s3Key, e.getMessage());
                s3ImageService.saveDeleteErrorLog(s3Key);
            }
        }
    }

    //TODO:(S3 삭제 실패 시 주기적 검사로 처리)

}
