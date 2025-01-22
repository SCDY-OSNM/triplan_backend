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
import scdy.reservationservice.enums.ReservationStatus;
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
        boolean isAdmin = user.getUserRole().equals("ADMIN");
        boolean isHost = user.getUserRole().equals("HOST");
        boolean isOwner = contents.getUserId().equals(userId);

        if(!(isAdmin || (isHost && isOwner))){
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
        boolean isAdmin = user.getUserRole().equals("ADMIN");
        boolean isHost = user.getUserRole().equals("HOST");
        boolean isOwner = contents.getUserId().equals(userId);

        if(!(isAdmin || (isHost && isOwner))){
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
        boolean isAdmin = user.getUserRole().equals("ADMIN");
        boolean isHost = user.getUserRole().equals("HOST");
        boolean isOwner = contents.getUserId().equals(userId);
        boolean isReservedUser = reservation.getUserId().equals(userId);

        if(!(isAdmin || (isHost && isOwner) || isReservedUser)){
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
        boolean isAdmin = user.getUserRole().equals("ADMIN");
        boolean isHost = user.getUserRole().equals("HOST");
        boolean isOwner = contents.getUserId().equals(userId);

        if(!(isAdmin || (isHost && isOwner))){
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
        boolean isAdmin = user.getUserRole().equals("ADMIN");
        boolean isUserMatched = reservationRequestDto.getUserId().equals(userId);

        if(!(isAdmin || (isUserMatched))) {
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

        List<Reservation> reservationList = reservationRepository.findAllByResDateAndContentsId(
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
        boolean isAdmin = user.getUserRole().equals("ADMIN");
        boolean isHost = user.getUserRole().equals("HOST");
        boolean isOwner = contents.getUserId().equals(userId);

        if(!(isAdmin || (isHost && isOwner))){
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
        boolean isAdmin = user.getUserRole().equals("ADMIN");
        boolean isHost = user.getUserRole().equals("HOST");
        boolean isOwner = contents.getUserId().equals(userId);
        boolean isMatchedUser = reservation.getUserId().equals(userId);

        if(!(isAdmin || (isHost && isOwner) || isMatchedUser)) {
            throw new BadRequestException("취소 권한이 없는 사용자입니다.");
        }

        if(reservation.getReservationStartAt().isBefore(LocalDateTime.now())){
            throw new BadRequestException("지난 예약은 취소할 수 없습니다.");
        }

        reservation.cancelReservation();
        return ReservationResponseDto.from(reservation);
    }


    //Reservation Batch Func

}
