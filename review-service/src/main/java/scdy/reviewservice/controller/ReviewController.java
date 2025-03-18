package scdy.reviewservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scdy.reviewservice.common.advice.ApiResponse;
import scdy.reviewservice.dto.ReviewRequestDto;
import scdy.reviewservice.dto.ReviewResponseDto;
import scdy.reviewservice.service.ReviewService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    //리뷰 작성
    @PostMapping()
    public ResponseEntity<ApiResponse<ReviewResponseDto>> createReview(@RequestHeader("X-Authenticated-User") Long userId,
                                                                       @RequestBody ReviewRequestDto reviewRequestDto) {
        ReviewResponseDto reviewResponseDto = reviewService.createReview(userId, reviewRequestDto);
        return ResponseEntity.ok(ApiResponse.success("리뷰 작성 성공", reviewResponseDto));
    }

    //리뷰 수정
    @PatchMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponseDto>> updateReview(@RequestHeader ("X-Authenticated-User")  Long userId,
                                                                       @RequestHeader ("X-User-Role")String userRole,
                                                                       @RequestBody ReviewRequestDto reviewRequestDto,
                                                                       @PathVariable("reviewId") Long reviewId){
        ReviewResponseDto reviewResponseDto = reviewService.updateReview(reviewRequestDto, userId, reviewId, userRole);
        return ResponseEntity.ok(ApiResponse.success("리뷰 수정 완료", reviewResponseDto));
    }


    //단일 리뷰 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponseDto>> getReview(@RequestHeader("X-Authenticated-User") Long userId,
                                                                    @PathVariable("reviewId") Long reviewId){

        ReviewResponseDto reviewResponseDto = reviewService.getReviewById(reviewId);
        return ResponseEntity.ok(ApiResponse.success("리뷰 조회 성공", reviewResponseDto));
    }

    //컨텐츠 별 리뷰 조회
    @GetMapping("/content/{contentsId}")
    public ResponseEntity<ApiResponse<Page<ReviewResponseDto>>> getReviewByContent(@RequestHeader ("X-User-Role")String userRole,
                                                                                   @PathVariable("contentsId") Long contentsId,
                                                                                   Pageable pageable){
        Page<ReviewResponseDto> reviewResponseDto = reviewService.getReviewsByContentsId(contentsId, pageable);
        return ResponseEntity.ok(ApiResponse.success("컨텐츠 리뷰 조회 완료", reviewResponseDto));
    }

    //사용자 별 리뷰 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Page<ReviewResponseDto>>> getReviewByUserId(@PathVariable("userId") Long reviewUserId,
                                                                                  Pageable pageable){

        Page<ReviewResponseDto> reviewResponseDtoPage = reviewService.getReviewsByUserId(reviewUserId, pageable);
        return ResponseEntity.ok(ApiResponse.success("유저 리뷰 조회 성공", reviewResponseDtoPage));
    }

    //리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponseDto>> deleteReview(@RequestHeader ("X-Authenticated-User")  Long userId,
                                                                       @RequestHeader ("X-User-Role")String userRole,
                                                                       @PathVariable("reviewId") Long reviewId){
        ReviewResponseDto reviewResponseDto = reviewService.deleteReview(reviewId, userId, userRole);
        return ResponseEntity.ok(ApiResponse.success("리뷰 삭제 성공", reviewResponseDto));
    }
}
