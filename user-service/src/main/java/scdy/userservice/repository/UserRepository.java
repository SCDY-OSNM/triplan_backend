package scdy.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scdy.userservice.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    User findByKakaoId(Long kakaoId);

}
