package scdy.configservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scdy.configservice.entity.Image;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByUserId(Long userId);

    List<Image> findAllByBoardId(Long boardId);

    List<Image> findAllByContentsId(Long contentsId);
}
