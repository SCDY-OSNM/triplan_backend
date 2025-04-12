package scdy.configservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import scdy.configservice.common.advice.ApiResponse;
import scdy.configservice.service.S3ImageService;

@RequiredArgsConstructor
@RestController("/api/v1/images")
public class ImageController {

    private final S3ImageService s3ImageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<String>> uploadImage(@RequestPart(value = "image", required = false) MultipartFile image) {
        String profileImage = s3ImageService.upload(image);
        return ResponseEntity.ok(ApiResponse.success("이미지가 업로드 되었습니다.",profileImage));
    }

    @GetMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteImage(@RequestParam String imagePath) {
        s3ImageService.deleteImageFromS3(imagePath);
        return ResponseEntity.ok(ApiResponse.success("이미지가 삭제되었습니다.",null));
    }

}
