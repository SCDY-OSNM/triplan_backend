package scdy.configservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import scdy.configservice.Exception.LackOfImageInfoException;
import scdy.configservice.Exception.S3Exception;
import scdy.configservice.dto.ImageRequestDto;
import scdy.configservice.dto.ImageResponseDto;
import scdy.configservice.entity.Image;
import scdy.configservice.enums.ImageType;
import scdy.configservice.repository.ImageRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final S3ImageService s3ImageService;

    @Transactional
    public ImageResponseDto saveImageInfo(MultipartFile image, ImageRequestDto imageRequestDto) {

        Image imageEntity = null;
        String s3Url = null;

        try{
            //이미지 s3업로드
            s3Url = s3ImageService.upload(image);

            //이미지 타입 검증
            if(imageRequestDto.getImageType() == ImageType.USER) {
                if (imageRequestDto.getUserId() == null) {
                    throw new LackOfImageInfoException("사용자 정보가 없습니다.");
                }
            }
            else if (imageRequestDto.getImageType() == ImageType.BOARD) {
                if (imageRequestDto.getBoardId() == null) {
                    throw new LackOfImageInfoException("게시글 정보가 부족합니다");
                }
            }
            else if (imageRequestDto.getImageType() == ImageType.CONTENTS) {
                if (imageRequestDto.getContentsId() == null) {
                    throw new LackOfImageInfoException("콘텐츠 정보가 부족합니다");
                }
            }

            //DB 저장
            imageEntity = Image.builder()
                .imageType(imageRequestDto.getImageType())
                .imageUrl(s3Url)
                .imageSize((int)image.getSize())
                .userId(imageRequestDto.getUserId())
                .boardId(imageRequestDto.getBoardId())
                .contentsId(imageRequestDto.getContentsId())
                .build();

            imageRepository.save(imageEntity);
        }catch (Exception e){
            //DB 저장 실패 시 S3 롤백 보상
            if(s3Url != null){ //s3 업로드는 성공헀으나, DB에 저장 실패 한 경우
                try{
                    s3ImageService.deleteImageFromS3(s3Url);
                    log.error("[WARNING] S3 파일 업로드 후 DB 저장 실패로 파일 업로드 롤백: " + image.getOriginalFilename());
                }catch (Exception deleteEx){
                    log.error("[ERROR] S3 파일 롤백 실패: " + image.getOriginalFilename() + " | " + deleteEx.getMessage());
                }
            }
            log.error("[WARNING] 이미지 업로드 실패" + e);
            throw new S3Exception("이미지 업로드 및 저장 실패");
        }

        return ImageResponseDto.from(imageEntity);
    }

//이미지 삭제 시 정합성(DB 먼저 삭제 후 s3에서 삭제.)
    //(S3 삭제 실패 시 주기적 검사로 처리)
}
