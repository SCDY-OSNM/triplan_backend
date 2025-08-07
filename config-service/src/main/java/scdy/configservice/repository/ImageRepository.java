package scdy.configservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scdy.configservice.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
