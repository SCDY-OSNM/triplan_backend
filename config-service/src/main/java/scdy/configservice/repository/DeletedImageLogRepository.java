package scdy.configservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scdy.configservice.entity.DeletedImageLog;

public interface DeletedImageLogRepository extends JpaRepository<DeletedImageLog, Long> {

}
