package scdy.reservationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scdy.reservationservice.client.ContentsClient;
import scdy.reservationservice.client.UserClient;
import scdy.reservationservice.dto.*;
import scdy.reservationservice.entity.Reservation;
import scdy.reservationservice.entity.enums.ReservationStatus;
import scdy.reservationservice.exception.NotfoundPermissionException;
import scdy.reservationservice.exception.ReservationBadRequestException;
import scdy.reservationservice.repository.ReservationRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserClient userClient;
    private final ContentsClient contentsClient;
    private final RabbitTemplate rabbitTemplate;

    //Create Reservation
    //Only Admin and Owned Host can create Reservation
    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto reservationRequestDto, Long userId, String userRole) {
        ContentResponseDto contents = getContents(reservationRequestDto.getContentsId());

        //check user
        //생성 시 예약자 체크 불필요
        if (!checkUserPermission(userRole, contents.getUserId(), null, userId)) {
            throw new NotfoundPermissionException("권한이 없는 사용자입니다.");
        }

        Reservation reservation = Reservation.builder()
                .userId(userId)
                .contentsId(reservationRequestDto.getContentsId())
                .planDetailId(reservationRequestDto.getPlanDetailId())
                .reservationStartAt(reservationRequestDto.getReservationStartAt())
                .reservationEndAt(reservationRequestDto.getReservationEndAt())
                .reservationStatus(ReservationStatus.NOT_RESERVED)
                .build();

        reservationRepository.save(reservation);
        return ReservationResponseDto.from(reservation);
    }

    //Update Reservation
    //Only Admin and Owned Host can update Reservation
    @Transactional
    public ReservationResponseDto updateReservation(Long reservationId, ReservationRequestDto reservationRequestDto, Long userId, String userRole) {
        Reservation reservation = getReservation(reservationId);
        ContentResponseDto contents = getContents(reservation.getContentsId());

        //check user
        //수정 시 예약 사용자 확인 불필요
        if (!checkUserPermission(userRole, contents.getUserId(), null, userId)) {
            throw new NotfoundPermissionException("권한이 없는 사용자입니다.");
        }

        reservation.updateReservation(
                reservationRequestDto.getPlanDetailId()!= null
                        ? reservationRequestDto.getPlanDetailId() : reservation.getPlanDetailId(),
                reservationRequestDto.getReservationStartAt()!= null
                        ? reservationRequestDto.getReservationStartAt() : reservation.getReservationStartAt(),
                reservationRequestDto.getReservationEndAt() !=null
                        ? reservationRequestDto.getReservationEndAt() : reservation.getReservationEndAt(),
                reservationRequestDto.getReservationStatus() != null
                        ?  reservationRequestDto.getReservationStatus() : reservation.getReservationStatus()
        );
        return ReservationResponseDto.from(reservation);
    }

    //Get reservation
    //Only Admin, owned host and reserved user can get reservation
    public ReservationResponseDto getReservationById(Long reservationId, Long userId, String userRole) {
        Reservation reservation = reservationRepository.findByIdOrElseThrow(reservationId);
        ContentResponseDto contents = contentsClient.getContentsById(reservationId).getData();

        //check user
        if (!checkUserPermission(userRole, contents.getUserId(), reservation.getUserId(), userId )) {
            throw new NotfoundPermissionException("권한이 없는 사용자입니다.");
        }

        return ReservationResponseDto.from(reservation);
    }

    //Get Reservation By Contents
    //Only Admin and Owned Host can get reservation list
    public List<ReservationResponseDto> getReservationListByContentsId(Long contentId, Long userId, String userRole) {
        ContentResponseDto contents = contentsClient.getContentsById(contentId).getData();

        //check user
        // 목록 조회 시에는 예약 사용자 확인 불필요
        if (!checkUserPermission(userRole, contents.getUserId(), null, userId)) {
            throw new NotfoundPermissionException("권한이 없는 사용자입니다.");
        }

        List<Reservation> reservationList = reservationRepository.getReservationListByContentsId(contentId);

        return reservationList.stream()
                .map(ReservationResponseDto::from)
                .toList();
    }



    //Get Reservation by User
    //Only Admin and user themselves can get reservation list
    public List<ReservationResponseDto> getReservationListByUserId(Long currentUserId, Long userId, String userRole) {
        userClient.getUserById(userId);

        //check user
        if(!"ADMIN".equalsIgnoreCase(userRole) && !Objects.equals(userId, currentUserId)){
            System.out.println("userRole = " + userRole);
            throw new NotfoundPermissionException("조회 권한이 없는 사용자입니다.");
        }

        List<Reservation> reservationList = reservationRepository.getReservationListByUserId(userId);

        return reservationList.stream()
                .map(ReservationResponseDto::from)
                .toList();
    }

    //Get daily reservation list by contentsId
    public List<ReservationResponseDto> getDailyReservationListByContentsId(ReservationRequestDto dto) {
        LocalDateTime startOfDay = dto.getReservationStartAt().toLocalDate().atStartOfDay();
        ContentResponseDto contents = contentsClient.getContentsById(dto.getContentsId()).getData();

        List<Reservation> reservationList = reservationRepository.findAllByReservationDateAndContentsId(
                startOfDay,
                startOfDay.plusDays(1),
                contents.getContentId()
        );

        return reservationList.stream()
                .map(ReservationResponseDto::from)
                .toList();
    }


    //Delete reservation
    //Only Admin and owned Host can delete reservation
    @Transactional
    public void deleteReservation(Long reservationId, Long userId, String userRole) {
        Reservation reservation = reservationRepository.findByIdOrElseThrow(reservationId);
        ContentResponseDto contents = contentsClient.getContentsById(reservationId).getData();

        //check user
        if (!checkUserPermission(userRole, contents.getUserId(), null, userId)) {
            throw new NotfoundPermissionException("권한이 없는 사용자입니다.");
        }

        //Check Reservation Status
        if( !reservation.getReservationStatus().equals(ReservationStatus.NOT_RESERVED)) {
            throw new ReservationBadRequestException("예약된 상태에서는 삭제할 수 없습니다.");
        }

        //Check reservation time
        if(reservation.getReservationStartAt().isBefore(LocalDateTime.now())){
            throw new ReservationBadRequestException("지난 예약은 삭제할 수 없습니다. 신청되지 않은 예약은 추후 일괄 삭제됩니다.");
        }

        //delete reservation
        reservationRepository.delete(reservation);
    }

    //Make a reservation
    @Transactional
    public ReservationResponseDto makeReservation(Long reservationId, Long userId) {
        UserResponseDto user = userClient.getUserById(userId).getData();
        Reservation reservation = reservationRepository.findByIdOrElseThrow(reservationId);

        //Check the availability of reservation
        if(!reservation.getReservationStatus().equals(ReservationStatus.NOT_RESERVED)) {
            throw new ReservationBadRequestException("이미 선점된 예약입니다");
        }

        //Check the time of reservation
        if(reservation.getReservationStartAt().isBefore(LocalDateTime.now())){
            throw new ReservationBadRequestException("지난 시간은 예약할 수 없습니다.");
        }

        //make reservation
        reservation.makeReservation(user.getUserId());

        // RabbitMQ로 예약 완료 알림 전송
        ContentResponseDto contents = getContents(reservation.getContentsId());
        if(contents==null){
            log.warn("콘텐츠가 존재하지 않습니다. contentId : {}", reservation.getContentsId());
        }
        if (contents.getContentName() == null) {
            log.warn("콘텐츠 이름이 null입니다. contentsId: {}", reservation.getContentsId());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");

        String noticeBody = reservation.getReservationStartAt().format(formatter)
                + "에 <" + contents.getContentName() + "> 예약이 완료되었습니다.";

        NotificationMessage notificationMessage = NotificationMessage.builder()
                .noticeTitle("예약이 완료되었습니다.")
                .noticeBody(noticeBody)
                .userId(userId)
                .noticedAt(LocalDateTime.now())
                .build();

        rabbitTemplate.convertAndSend("NotificationExchange", "key", notificationMessage);
        log.info("예약 완료 알림 메시지를 전송했습니다. 대상 userId: {}, 내용: {}", userId, noticeBody);

        return ReservationResponseDto.from(reservation);
    }


    //Cancel Reservation
    //Only Admin, Owned Host and reserved user can cancel reservation
    @Transactional
    public ReservationResponseDto cancelReservation(Long reservationId, Long userId, String userRole) {
        Reservation reservation = reservationRepository.findByIdOrElseThrow(reservationId);
        ContentResponseDto contents = contentsClient.getContentsById(reservation.getContentsId()).getData();

        //check the reservation
        if(reservation.getReservationStatus().equals(ReservationStatus.NOT_RESERVED)) {
            throw new ReservationBadRequestException("신청되지 않은 예약은 취소할 수 없습니다.");
        }

        //check user
        if (!checkUserPermission(userRole, contents.getUserId(), reservation.getUserId(), userId)) {
            throw new NotfoundPermissionException("취소 권한이 없는 사용자입니다.");
        }

        if(reservation.getReservationStartAt().isBefore(LocalDateTime.now())){
            throw new ReservationBadRequestException("지난 예약은 취소할 수 없습니다.");
        }

        reservation.cancelReservation();
        return ReservationResponseDto.from(reservation);
    }


    //Reservation Batch Func


    //Permission Check
    private boolean checkUserPermission(String userRole, Long contentsUserId, Long reservedUserId, Long requestUserId) {
        boolean isAdmin = userRole.equals("ADMIN");
        boolean isHost = userRole.equals("HOST");
        boolean isOwner = contentsUserId.equals(requestUserId);
        boolean isReservedUser = Objects.equals(reservedUserId, requestUserId);

        return isAdmin || (isHost && isOwner) || isReservedUser;
    }


    //Feign Client 호출
    private UserResponseDto getUser(Long userId) {
        return userClient.getUserById(userId).getData();
    }

    private ContentResponseDto getContents(Long contentsId) {
        return contentsClient.getContentsById(contentsId).getData();
    }

    private Reservation getReservation(Long reservationId) {
        return reservationRepository.findByIdOrElseThrow(reservationId);
    }

}
