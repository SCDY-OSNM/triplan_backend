package scdy.configservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import scdy.configservice.common.advice.ApiResponse;
import scdy.configservice.dto.ImageRequestDto;
import scdy.configservice.dto.ImageResponseDto;
import scdy.configservice.service.ImageService;
import scdy.configservice.service.S3ImageService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
public class ImageController {

    private final ImageService imageService;
    private final S3ImageService s3ImageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<ImageResponseDto>> uploadImage(
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart("imageRequestDto") ImageRequestDto imageRequestDto
    ) {
        ImageResponseDto dto = imageService.upload(image, imageRequestDto);
        return ResponseEntity.ok(ApiResponse.success("이미지가 업로드 되었습니다.",dto));
    }

    @GetMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteImage(@RequestParam String imagePath) {
        s3ImageService.deleteImageFromS3(imagePath);
        return ResponseEntity.ok(ApiResponse.success("이미지가 삭제되었습니다.",null));
    }

}
