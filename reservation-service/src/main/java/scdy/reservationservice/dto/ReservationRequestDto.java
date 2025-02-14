package scdy.reservationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.reservationservice.entity.enums.ReservationStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDto {
    private Long reservationId;

    private Long userId;

    private Long contentsId;

    private Long planDetailId;

    private LocalDateTime reservationStartAt;

    private LocalDateTime reservationEndAt;

    private ReservationStatus reservationStatus;

    @Builder
    public ReservationRequestDto(Long contentsId, Long planDetailId, LocalDateTime reservationStartAt, LocalDateTime reservationEndAt,
                                 ReservationStatus reservationStatus) {
        this.contentsId = contentsId;
        this.planDetailId = planDetailId;
        this.reservationStartAt = reservationStartAt;
        this.reservationEndAt = reservationEndAt;
        this.reservationStatus = reservationStatus;
    }
}
