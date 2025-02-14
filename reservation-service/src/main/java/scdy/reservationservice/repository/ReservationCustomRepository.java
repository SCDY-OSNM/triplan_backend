package scdy.reservationservice.repository;

import scdy.reservationservice.entity.Reservation;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationCustomRepository {

    List<Reservation> getReservationListByContentsId(Long contentsId);

    List<Reservation> getReservationListByUserId(Long userId);

    List<Reservation> findAllByReservationDateAndContentsId(LocalDateTime startOfDay, LocalDateTime startOfNextDay, Long contentsId);

    Reservation findByIdOrElseThrow(Long id);
}
