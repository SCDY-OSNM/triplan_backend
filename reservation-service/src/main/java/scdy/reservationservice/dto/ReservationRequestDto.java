package scdy.reservationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scdy.reservationservice.enums.ReservationStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDto {
    private Long userId;

    private Long contentsId;

    private Long planDetailId;

    private LocalDateTime reservationStartAt;

    private LocalDateTime reservationEndAt;

    private ReservationStatus reservationStatus;
}
