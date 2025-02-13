package scdy.reservationservice.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scdy.reservationservice.entity.QReservation;
import scdy.reservationservice.entity.Reservation;
import scdy.reservationservice.exception.ReservationNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ReservationCustomRepositoryImpl implements ReservationCustomRepository{

    private final JPAQueryFactory queryFactory;
    static final QReservation reservation = QReservation.reservation;

    @Override
    public List<Reservation> getReservationListByContentsId(Long contentsId) {

        return queryFactory
                .selectFrom(reservation)
                .where(reservation.contentsId.eq(contentsId))
                .fetch();
    }

    @Override
    public List<Reservation> getReservationListByUserId(Long userId) {

        return queryFactory
                .selectFrom(reservation)
                .where(reservation.userId.eq(userId))
                .fetch();
    }

    @Override
    public List<Reservation> findAllByReservationDateAndContentsId(
            LocalDateTime startOfDay,
            LocalDateTime startOfNextDay,
            Long contentsId
    ) {
        return queryFactory
                .selectFrom(reservation)
                .where(reservation.reservationStartAt.between(startOfDay, startOfNextDay.minusNanos(1)))
                .fetch();
    }

    @Override
    public Reservation findByIdOrElseThrow(Long id) {
        Reservation result = queryFactory
                .select(reservation)
                .where(reservation.reservationId.eq(id))
                .fetchFirst();

        if(result == null) {
            throw new ReservationNotFoundException("존재하지 않는 예약입니다.");
        }
        return result;
    }
}
