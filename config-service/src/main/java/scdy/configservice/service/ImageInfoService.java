package scdy.configservice.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import scdy.configservice.Exception.ImageNotFoundException;
import scdy.configservice.Exception.S3Exception;
import scdy.configservice.dto.ImageRequestDto;
import scdy.configservice.dto.ImageResponseDto;
import scdy.configservice.entity.Image;
import scdy.configservice.repository.ImageRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageInfoService {

    private final ImageRepository imageRepository;
    private final S3ImageService s3ImageService;

    //이미지 정보 저장 로직
    @Transactional
    public ImageResponseDto saveImageInfo(MultipartFile image, ImageRequestDto imageRequestDto, String s3Url, String s3Key) {

        Image imageEntity = null;

        try{

            //DB 저장
            imageEntity = Image.builder()
                    .imageType(imageRequestDto.getImageType())
                    .imageUrl(s3Url)
                    .s3Key(s3Key)
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
                    log.error("[WARNING] S3 파일 업로드 후 DB 저장 실패로 파일 업로드 롤백: {}", image.getOriginalFilename());
                }catch (Exception deleteEx){
                    log.error("[ERROR] S3 파일 롤백 실패: {}", image.getOriginalFilename(), deleteEx);
                }
            }
            log.error("[WARNING] 이미지 업로드 실패", e);
            throw new S3Exception("이미지 업로드 및 저장 실패");
        }

        return ImageResponseDto.from(imageEntity);
    }


    //이미지 정보 삭제 로직
    @Transactional
    public void deleteImageInfo(Long imageId){

        Image image = imageRepository.findById(imageId).orElseThrow(() -> new ImageNotFoundException("존재하지 않는 이미지"));
        imageRepository.delete(image);
    }

}
