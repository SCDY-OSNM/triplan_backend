package scdy.notificationservice.repository;

import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import scdy.notificationservice.common.exceptions.NotFoundException;
import scdy.notificationservice.entity.FcmToken;

import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    @Query("SELECT f FROM FcmToken f WHERE f.userId = :userId AND f.isActive = true ORDER BY f.createdAt DESC LIMIT 1")
    Optional<FcmToken> findLatestByUserId(@Param("userId") Long userId);

    default FcmToken findLatestByUserIdOrElseThrow(Long userId){
        return findLatestByUserId(userId).orElseThrow(
                ()-> new NotFoundException("토큰을 찾을 수 없습니다."));
    }

    default FcmToken findByIdOrElseThrow(Long fcmTokenId){
        return findById(fcmTokenId).orElseThrow(
                ()-> new NotFoundException("토큰을 찾을 수 없습니다."));
    }
}
