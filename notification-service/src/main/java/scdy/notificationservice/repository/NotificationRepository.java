package scdy.notificationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scdy.notificationservice.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
}
