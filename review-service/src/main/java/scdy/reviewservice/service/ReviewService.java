package scdy.reviewservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scdy.reviewservice.dto.ReviewRequestDto;
import scdy.reviewservice.dto.ReviewResponseDto;
import scdy.reviewservice.entity.Review;
import scdy.reviewservice.exception.PermissionNotFoundException;
import scdy.reviewservice.repository.ReviewRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;

    //create Review
    @Transactional
    public ReviewResponseDto createReview(Long userId, ReviewRequestDto reviewRequestDto){

        Review review = Review.builder()
                .userId(userId)
                .contentsId(reviewRequestDto.getContentsId())
                .content(reviewRequestDto.getContent())
                .star(reviewRequestDto.getStar())
                .build();
        reviewRepository.save(review);

        return ReviewResponseDto.from(review);
    }

    //update Review
    //Only ADMIN or Owner can update review
    @Transactional
    public ReviewResponseDto updateReview(ReviewRequestDto reviewRequestDto, Long userId, Long reviewId, String userRole){

        Review review = getReview(reviewId);

        //permission Check
        if(!isAdmin(userRole) && !isOwner(review.getUserId(), userId)){
            throw new PermissionNotFoundException("리뷰 수정 권한이 없는 사용자입니다.");
        }

        String newContents = reviewRequestDto.getContent();
        Integer newStar = reviewRequestDto.getStar();

        //null check
        if(newContents == null) newContents = review.getContent();
        if(newStar == null) newStar = review.getStar();

        review.update(newContents, newStar);

        return ReviewResponseDto.from(review);
    }

    //get Review by reviewId
    public ReviewResponseDto getReviewById(Long reviewId){

        Review review = getReview(reviewId);

        return ReviewResponseDto.from(review);
    }

    //get Review By ContentsId (sort by recent / star)
    public Page<ReviewResponseDto> getReviewsByContentsId(Long contentsId , Pageable pageable){

        Page<Review> reviewPage = reviewRepository.getReviewsByContentsId(contentsId, pageable);

        return reviewPage.map(ReviewResponseDto::from);

    }

    //get Review By UserId (sort by recent)
    public Page<ReviewResponseDto> getReviewsByUserId(Long userId, Pageable pageable){

        Page<Review> reviewPage = reviewRepository.getReviewsByUserId(userId, pageable);

        return reviewPage.map(ReviewResponseDto::from);
    }

    //delete Review
    @Transactional
    public ReviewResponseDto deleteReview(Long reviewId, Long userId, String userRole){

        Review review = getReview(reviewId);

        if(!isOwner(review.getUserId(), userId)&& !isAdmin(userRole)){
            throw new PermissionNotFoundException("리뷰 삭제 권한이 없는 사용자입니다.");
        }

        reviewRepository.deleteById(reviewId);
        return ReviewResponseDto.from(review);
    }


    //메서드
    private Review getReview(Long reviewId){

        return reviewRepository.getReviewByIdOrElseThrow(reviewId);
    }

    private Boolean isAdmin(String userRole){

        return userRole == "ADMIN";
    }

    private Boolean isOwner(Long reviewUserId, Long requestUserId){

        return reviewUserId == requestUserId;
    }

}
