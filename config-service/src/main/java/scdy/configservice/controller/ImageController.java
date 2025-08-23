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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
public class ImageController {

    private final ImageService imageService;
    private final S3ImageService s3ImageService;

    //이미지 업로드
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<ImageResponseDto>> uploadImage(
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart("imageRequestDto") ImageRequestDto imageRequestDto
    ) {

        ImageResponseDto dto = imageService.upload(image, imageRequestDto);
        return ResponseEntity.ok(ApiResponse.success("이미지가 업로드 되었습니다.",dto));
    }

    // 단일 이미지 삭제
    @DeleteMapping("/{imageId}")
    public ResponseEntity<ApiResponse<Void>> deleteImageById(@PathVariable Long imageId) {

        imageService.deleteImageById(imageId);
        return ResponseEntity.ok(ApiResponse.success("이미지 삭제가 완료되었습니다.", null));
    }

    // 게시글에 속한 모든 이미지 삭제
    @DeleteMapping("/board/{boardId}")
    public ResponseEntity<ApiResponse<Void>> deleteImagesByBoardId(@PathVariable Long boardId) {

        imageService.deleteImagesByBoardId(boardId);
        return ResponseEntity.ok(ApiResponse.success("게시글 이미지가 삭제되었습니다.", null));
    }

    // 콘텐츠에 속한 모든 이미지 삭제
    @DeleteMapping("/contents/{contentsId}")
    public ResponseEntity<ApiResponse<Void>> deleteImagesByContentsId(@PathVariable Long contentsId) {

        imageService.deleteImagesByContentsId(contentsId);
        return ResponseEntity.ok(ApiResponse.success("콘텐츠 이미지가 삭제되었습니다.", null));
    }

    // 이미지 조회 - 사용자 프로필
    @GetMapping("/profile/{userId}")
    public ResponseEntity<ApiResponse<ImageResponseDto>> getUserProfile(@PathVariable Long userId) {

        ImageResponseDto dto = imageService.getUserProfile(userId);
        return ResponseEntity.ok(ApiResponse.success("사용자 프로필 이미지를 불러왔습니다.", dto));
    }

    // 이미지 조회 - 게시글 별
    @GetMapping("/board/{boardId}")
    public ResponseEntity<ApiResponse<List<ImageResponseDto>>> getImagesByBoardId(@PathVariable Long boardId) {

        List<ImageResponseDto> dtoList = imageService.getImagesByBoardId(boardId);
        return ResponseEntity.ok(ApiResponse.success("게시글 이미지를 불러왔습니다.", dtoList));
    }

    // 이미지 조회 - 콘텐츠 별
    @GetMapping("/contents/{contentsId}")
    public ResponseEntity<ApiResponse<List<ImageResponseDto>>> getImagesByContentsId(@PathVariable Long contentsId) {

        List<ImageResponseDto> dtoList = imageService.getImagesByContentsId(contentsId);
        return ResponseEntity.ok(ApiResponse.success("콘텐츠 이미지를 불러왔습니다.", dtoList));
    }

}
