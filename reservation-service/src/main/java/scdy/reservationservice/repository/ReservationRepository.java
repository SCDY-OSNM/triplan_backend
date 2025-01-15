package scdy.reservationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scdy.reservationservice.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {


}
