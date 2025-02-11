package scdy.reservationservice.repository;

import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import scdy.reservationservice.entity.Reservation;
import scdy.reservationservice.exception.ReservationNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationCustomRepository {

//    @Query("SELECT r FROM Reservation r WHERE r.contentsId = :contentsId")
//    List<Reservation> getReservationListByContentsId(@Param("contentsId") Long contentsId);
//
//    @Query("SELECT r FROM Reservation r WHERE r.userId = :userId")
//    List<Reservation> getReservationListByUserId(@Param("userId") Long userId);
//
//    @Query("SELECT r FROM Reservation r WHERE r.reservationStartAt >= :startOfDay AND r.reservationStartAt < :startOfNextDay AND r.contentsId = :contentsId")
//    List<Reservation> findAllByResDateAndContentsId(
//            @Param("startOfDay") LocalDateTime startOfDay,
//            @Param("startOfNextDay") LocalDateTime startOfNextDay,
//            @Param("contentsId") Long contentsId
//    );
//
//    default Reservation findByIdOrElseThrow(Long id) {
//        return findById(id).orElseThrow(() -> new ReservationNotFoundException("존재하지 않는 예약입니다."));
//    }
}
