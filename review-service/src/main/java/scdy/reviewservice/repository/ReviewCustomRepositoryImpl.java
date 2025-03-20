package scdy.reviewservice.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import scdy.reviewservice.entity.QReview;
import scdy.reviewservice.entity.Review;
import scdy.reviewservice.exception.ReviewNotFoundException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final QReview review = QReview.review;

    @Override
    public Page<Review> getReviewsByContentsId(Long contentsId, Pageable pageable) {

        List<Review> result = queryFactory
                .selectFrom(review)
                .where(review.contentsId.eq(contentsId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(review.count())
                .from(review)
                .where(review.contentsId.eq(contentsId))
                .fetchOne();

        return new PageImpl<Review>(result, pageable, count != null ? count : 0L);
    }

    @Override
    public Page<Review> getReviewsByUserId(Long userId, Pageable pageable) {

        List<Review> reviews = queryFactory
                .selectFrom(review)
                .where(review.userId.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = Optional.ofNullable(queryFactory
                .select(review.count())
                .from(review)
                .where(review.userId.eq(userId))
                .fetchOne()).orElse(0L);

        return new PageImpl<Review>(reviews, pageable, count);
    }

    @Override
    public Review getReviewByIdOrElseThrow(Long reviewId) {

        Review result = queryFactory
                .select(review)
                .from(review)
                .where(review.reviewId.eq(reviewId))
                .fetchFirst();

        if(result == null) {
            throw new ReviewNotFoundException("존재하지 않는 리뷰입니다.");
        }
        return result;
    }
}
