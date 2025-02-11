package scdy.reservationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scdy.reservationservice.client.ContentsClient;
import scdy.reservationservice.client.UserClient;
import scdy.reservationservice.common.exceptions.BadRequestException;
import scdy.reservationservice.dto.ContentsResponseDto;
import scdy.reservationservice.dto.ReservationRequestDto;
import scdy.reservationservice.dto.ReservationResponseDto;
import scdy.reservationservice.dto.UserResponseDto;
import scdy.reservationservice.entity.Reservation;
import scdy.reservationservice.entity.enums.ReservationStatus;
import scdy.reservationservice.repository.ReservationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserClient userClient;
    private final ContentsClient contentsClient;

    //Create Reservation
    //Only Admin and Owned Host can create Reservation
    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto reservationRequestDto, Long userId) {
        UserResponseDto user = userClient.getUserById(userId).getData();
        ContentsResponseDto contents = contentsClient.getContentsById(reservationRequestDto.getContentsId()).getData();

        //check user
        //생성 시에는 사용자 확인 불필요
        if (!checkUserPermission(user, contents, null)) {
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

    //Update Reservation
    //Only Admin and Owned Host can update Reservation
    @Transactional
    public ReservationResponseDto updateReservation(ReservationRequestDto reservationRequestDto, Long userId) {
        UserResponseDto user = userClient.getUserById(userId).getData();
        Reservation reservation = reservationRepository.findByIdOrElseThrow(reservationRequestDto.getReservationId());
        ContentsResponseDto contents = contentsClient.getContentsById(reservationRequestDto.getContentsId()).getData();

        //check user
        //수정 시 예약 사용자 확인 불필요
        if (!checkUserPermission(user, contents, null)) {
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

    //Get reservation
    //Only Admin, owned host and reserved user can get reservation
    public ReservationResponseDto getReservationById(ReservationRequestDto reservationRequestDto, Long userId) {
        UserResponseDto user = userClient.getUserById(userId).getData();
        Reservation reservation = reservationRepository.findByIdOrElseThrow(reservationRequestDto.getReservationId());
        ContentsResponseDto contents = contentsClient.getContentsById(reservationRequestDto.getContentsId()).getData();

        //check user
        if (!checkUserPermission(user, contents, reservation.getUserId())) {
            throw new BadRequestException("권한이 없는 사용자입니다.");
        }

        return ReservationResponseDto.from(reservation);
    }

    //Get Reservation By Contents
    //Only Admin and Owned Host can get reservation list
    public List<ReservationResponseDto> getReservationListByContentsId(ReservationRequestDto reservationRequestDto, Long userId) {
        UserResponseDto user = userClient.getUserById(userId).getData();
        Reservation reservation = reservationRepository.findByIdOrElseThrow(reservationRequestDto.getReservationId());
        ContentsResponseDto contents = contentsClient.getContentsById(reservationRequestDto.getContentsId()).getData();


        //check user
        // 목록 조회 시에는 예약 사용자 확인 불필요
        if (!checkUserPermission(user, contents, null)) {
            throw new BadRequestException("권한이 없는 사용자입니다.");
        }


        List<Reservation> reservationList = reservationRepository.getReservationListByContentsId(reservation.getContentsId());

        return reservationList.stream()
                .map(ReservationResponseDto::from)
                .toList();
    }


    //Get Reservation by User
    //Only Admin and user themselves can get reservation list
    public List<ReservationResponseDto> getReservationListByUserId(ReservationRequestDto reservationRequestDto, Long userId) {
        UserResponseDto user = userClient.getUserById(userId).getData();

        //check user
        if (!checkUserPermission(user, null, reservationRequestDto.getUserId())) { // contents 필요없음
            throw new BadRequestException("권한이 없는 사용자입니다");
        }

        List<Reservation> reservationList = reservationRepository.getReservationListByUserId(reservationRequestDto.getUserId());

        return reservationList.stream()
                .map(ReservationResponseDto::from)
                .toList();
    }

    //Get daily reservation list by contentsId
    public List<ReservationResponseDto> getDailyReservationListByContentsId(ReservationRequestDto dto) {
        LocalDateTime startOfDay = dto.getReservationStartAt().toLocalDate().atStartOfDay();
        ContentsResponseDto contents = contentsClient.getContentsById(dto.getContentsId()).getData();

        List<Reservation> reservationList = reservationRepository.findAllByReservationDateAndContentsId(
                startOfDay,
                startOfDay.plusDays(1),
                contents.getContentsId()
        );

        return reservationList.stream()
                .map(ReservationResponseDto::from)
                .toList();
    }


    //Delete reservation
    //Only Admin and owned Host can delete reservation
    @Transactional
    public void deleteReservation(ReservationRequestDto reservationRequestDto, Long userId) {
        UserResponseDto user = userClient.getUserById(userId).getData();
        Reservation reservation = reservationRepository.findByIdOrElseThrow(reservationRequestDto.getReservationId());
        ContentsResponseDto contents = contentsClient.getContentsById(reservationRequestDto.getContentsId()).getData();

        //check user
        if (!checkUserPermission(user, contents, null)) {
            throw new BadRequestException("권한이 없는 사용자입니다.");
        }

        //Check Reservation Status
        if( !reservation.getReservationStatus().equals(ReservationStatus.NOT_RESERVED)) {
            throw new BadRequestException("예약된 상태에서는 삭제할 수 없습니다.");
        }

        //Check reservation time
        if(reservation.getReservationStartAt().isBefore(LocalDateTime.now())){
            throw new BadRequestException("지난 예약은 삭제할 수 없습니다. 신청되지 않은 예약은 추후 일괄 삭제됩니다.");
        }

        //delete reservation
        reservationRepository.delete(reservation);
    }

    //Make a reservation
    @Transactional
    public ReservationResponseDto makeReservation(ReservationRequestDto reservationRequestDto, Long userId) {
        UserResponseDto user = userClient.getUserById(userId).getData();
        Reservation reservation = reservationRepository.findByIdOrElseThrow(reservationRequestDto.getReservationId());

        if (!checkUserPermission(user, null, reservation.getUserId())) { // 예약 신청자는 본인인지 확인
            throw new BadRequestException("권한이 없는 사용자입니다.");
        }

        //Check the availability of reservation
        if( !reservation.getReservationStatus().equals(ReservationStatus.NOT_RESERVED)) {
            throw new BadRequestException("이미 선점된 예약입니다");
        }

        //Check the time of reservation
        if(reservation.getReservationStartAt().isBefore(LocalDateTime.now())){
            throw new BadRequestException("지난 시간은 예약할 수 없습니다.");
        }

        //make reservation
        reservation.makeReservation(user.getUserId());

        return ReservationResponseDto.from(reservation);
    }


    //Cancel Reservation
    //Only Admin, Owned Host and reserved user can cancel reservation
    @Transactional
    public ReservationResponseDto cancelReservation(ReservationRequestDto reservationRequestDto, Long userId) {
        UserResponseDto user = userClient.getUserById(userId).getData();
        Reservation reservation = reservationRepository.findByIdOrElseThrow(reservationRequestDto.getReservationId());
        ContentsResponseDto contents = contentsClient.getContentsById(reservation.getContentsId()).getData();

        //check the reservation
        if(reservation.getReservationStatus().equals(ReservationStatus.NOT_RESERVED)) {
            throw new BadRequestException("신청되지 않은 예약은 취소할 수 없습니다.");
        }

        //check user
        if (!checkUserPermission(user, contents, reservation.getUserId())) {
            throw new BadRequestException("취소 권한이 없는 사용자입니다.");
        }

        if(reservation.getReservationStartAt().isBefore(LocalDateTime.now())){
            throw new BadRequestException("지난 예약은 취소할 수 없습니다.");
        }

        reservation.cancelReservation();
        return ReservationResponseDto.from(reservation);
    }


    //Reservation Batch Func


    private boolean checkUserPermission(UserResponseDto user, ContentsResponseDto contents, Long reservationUserId) {
        boolean isAdmin = user.getUserRole().equals("ADMIN");
        boolean isHost = user.getUserRole().equals("HOST");
        boolean isOwner = contents.getUserId().equals(user.getUserId()); // user id와 contents의 user id 비교
        boolean isReservedUser = reservationUserId.equals(user.getUserId());

        return isAdmin || (isHost && isOwner) || isReservedUser;
    }
}
