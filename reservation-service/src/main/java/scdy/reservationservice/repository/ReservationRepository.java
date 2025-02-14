package scdy.reservationservice.repository;

import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import scdy.reservationservice.entity.Reservation;
import scdy.reservationservice.exception.ReservationNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationCustomRepository {

}
