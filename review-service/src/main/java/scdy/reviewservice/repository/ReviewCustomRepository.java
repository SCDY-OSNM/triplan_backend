package scdy.reviewservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import scdy.reviewservice.entity.Review;

public interface ReviewCustomRepository {

    Review getReviewByIdOrElseThrow(Long reviewId);

    Page<Review> getReviewsByContentsId(Long contentsId, Pageable pageable);

    Page<Review> getReviewsByUserId(Long userId, Pageable pageable);
}
