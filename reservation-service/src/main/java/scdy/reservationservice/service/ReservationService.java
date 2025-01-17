package scdy.reservationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scdy.reservationservice.client.UserClient;
import scdy.reservationservice.common.exceptions.BadRequestException;
import scdy.reservationservice.dto.ReservationRequestDto;
import scdy.reservationservice.dto.ReservationResponseDto;
import scdy.reservationservice.dto.UserResponseDto;
import scdy.reservationservice.entity.Reservation;
import scdy.reservationservice.enums.ReservationStatus;
import scdy.reservationservice.repository.ReservationRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserClient userClient;

    //Create Reservation
    //Only Admin and Host can create Reservation
    public ReservationResponseDto createReservation(ReservationRequestDto reservationRequestDto, Long userId) {
        UserResponseDto user = userClient.getUserById(userId).getData();
        //check user
        if(!(user.getUserRole().equals("ADMIN") || user.getUserRole().equals("HOST"))) {
            throw new BadRequestException("권한이 없는 사용자입니다.");
        }

        Reservation reservation = Reservation.builder()
                .userId(reservationRequestDto.getUserId())
                .contentsId(reservationRequestDto.getContentsId())
                .planDetailId(reservationRequestDto.getPlanDetailId())
                .reservationStartAt(reservationRequestDto.getReservationStartAt())
                .reservationEndAt(reservationRequestDto.getReservationEndAt())
                .reservationStatus(ReservationStatus.WAITING)
                .build();

        reservationRepository.save(reservation);
        return ReservationResponseDto.from(reservation);
    }

    //Edit Reservation
    //Only Admin and Owned Host can update Reservation
    public ReservationResponseDto updateReservation(ReservationRequestDto reservationRequestDto, Long userId) {
        UserResponseDto user = userClient.getUserById(userId).getData();
        Reservation reservation = reservationRepository.findByIdOrElseThrow(reservationRequestDto.getReservationId());

        //user check
        if( !(user.getUserRole().equals("ADMIN")) || !(user.getUserId().equals(reservationRequestDto.getUserId()))) {
            throw new BadRequestException("권한이 없는 사용자입니다.");
        }

        reservation.updateReservation(
                reservationRequestDto.getPlanDetailId(),
                reservationRequestDto.getReservationStartAt(),
                reservationRequestDto.getReservationEndAt(),
                reservationRequestDto.getReservationStatus()
        );
        return ReservationResponseDto.from(reservation);
    }

    //Get Reservation By Contents
    //Only Admin and Owned Host can get reservation list
    public List<ReservationResponseDto> getReservationListByContentsId(ReservationRequestDto reservationRequestDto, Long userId) {
        UserResponseDto user = userClient.getUserById(userId).getData();
        Reservation reservation = reservationRepository.findByIdOrElseThrow(reservationRequestDto.getReservationId());

        //user check
        if( !(user.getUserRole().equals("ADMIN")) || !(user.getUserId().equals(reservationRequestDto.getUserId()))) {
            throw new BadRequestException("권한이 없는 사용자입니다.");
        }

        List<Reservation> reservationList = reservationRepository.getReservationListByContentsId(reservationRequestDto.getContentsId());

        return reservationList.stream()
                .map(ReservationResponseDto::from)
                .toList();
    }


    //사용자별 예약 조회

    //예약 삭제

    //예약 하기

    //예약 취소

    //예약 배치함수

}
