package scdy.reviewservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scdy.reviewservice.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewCustomRepository {

}
