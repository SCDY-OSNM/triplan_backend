package scdy.reservationservice.controller;

import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scdy.reservationservice.common.advice.ApiResponse;
import scdy.reservationservice.dto.ReservationRequestDto;
import scdy.reservationservice.dto.ReservationResponseDto;
import scdy.reservationservice.service.ReservationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    //단일 예약 생성
    @PostMapping()
    public ResponseEntity<ApiResponse<ReservationResponseDto>> createReservation(@RequestHeader("X-Authenticated-User") Long userId,
                                                                                 @RequestHeader("X-User-Role") String role,
                                                                                 @RequestBody ReservationRequestDto dto){

        ReservationResponseDto responseDto = reservationService.createReservation(dto, userId, role);
        return ResponseEntity.ok(ApiResponse.success("예약 생성 성공", responseDto));
    }

    //예약 수정
    //일부만 수정할 수 있도록 PATCH 사용
    @PatchMapping("/{reservationId}")
    public ResponseEntity<ApiResponse<ReservationResponseDto>> updateReservation(@RequestHeader("X-Authenticated-User") Long userId,
                                                                                 @RequestHeader("X-User-Role") String role,
                                                                                 @PathVariable("reservationId")Long reservationId, @RequestBody ReservationRequestDto dto){

        ReservationResponseDto responseDto = reservationService.updateReservation(reservationId, dto, userId, role);
        return ResponseEntity.ok(ApiResponse.success("예약 수정 성공", responseDto));
    }

    //예약 조회
    //TODO:GET MAPPING 경로 수정
    @GetMapping("/{reservationId}")
    public ResponseEntity<ApiResponse<ReservationResponseDto>> getReservation(@RequestHeader("X-Authenticated-User") Long userId,
                                                                              @RequestHeader("X-User-Role") String role,
                                                                              @PathVariable("reservationId") Long reservationId) {

        ReservationResponseDto responseDto = reservationService.getReservationById(reservationId, userId, role);
        return ResponseEntity.ok(ApiResponse.success("예약 조회 성공", responseDto));
    }


    //콘텐츠 ID 별 예약 목록
    @GetMapping("/by-contents/{contentId}")
    public ResponseEntity<ApiResponse<List<ReservationResponseDto>>> getReservationListByContentsId(@RequestHeader("X-Authenticated-User") Long userId,
                                                                                                    @RequestHeader("X-User-Role") String role,
                                                                                                    @PathVariable("contentId") Long contentId) {

        List<ReservationResponseDto> responseDtoList = reservationService.getReservationListByContentsId(contentId, userId, role);
        return ResponseEntity.ok(ApiResponse.success("콘텐츠별 예약목록 조회 성공", responseDtoList));
    }

    //사용자 ID 별 예약 목록
    @GetMapping("/by-users/{userId}")
    public ResponseEntity<ApiResponse<List<ReservationResponseDto>>> getReservationListByUserId(@RequestHeader("X-Authenticated-User") Long currentUserId,
                                                                                                @RequestHeader("X-User-Role") String role,
                                                                                                @PathVariable("userId") Long userId) {

        List<ReservationResponseDto> responseDtoList = reservationService.getReservationListByUserId(currentUserId, userId, role);
        return ResponseEntity.ok(ApiResponse.success("사용자별 예약 목록 조회 성공", responseDtoList));
    }

    //일간 콘텐츠 별 예약 목록
    @GetMapping("/by-daily-contents")
    public ResponseEntity<ApiResponse<List<ReservationResponseDto>>> getDailyReservationByContentsId(@RequestBody ReservationRequestDto dto) {

        List<ReservationResponseDto> responseDtoList = reservationService.getDailyReservationListByContentsId(dto);
        return ResponseEntity.ok(ApiResponse.success("일간 콘텐츠별 예약 목록 조회 성공", responseDtoList));
    }

    //예약 삭제
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<ApiResponse<Void>> deleteReservation(@RequestHeader("X-Authenticated-User") Long userId,
                                                               @RequestHeader("X-User-Role") String role,
                                                               @PathVariable("reservationId") Long reservationId){

        reservationService.deleteReservation(reservationId, userId, role);
        return ResponseEntity.ok(ApiResponse.success("에약 삭제 성공"));
    }

    //예약 하기
    @GetMapping("/make/{reservationId}")
    public ResponseEntity<ApiResponse<ReservationResponseDto>> makeAReservation(@RequestHeader("X-Authenticated-User") Long userId,
                                                                                @PathVariable("reservationId") Long reservationId){

        ReservationResponseDto responseDto =  reservationService.makeReservation(reservationId, userId);
        return ResponseEntity.ok(ApiResponse.success("예약 성공", responseDto));
    }

    //예약 취소하기
    @GetMapping("/cancel/{reservationId}")
    public ResponseEntity<ApiResponse<ReservationResponseDto>> cancelReservation(@RequestHeader("X-Authenticated-User") Long userId,
                                                                                 @RequestHeader("X-User-Role") String role,
                                                                                 @PathVariable("reservationId") Long reservationId){

        ReservationResponseDto responseDto = reservationService.cancelReservation(reservationId, userId, role);
        return ResponseEntity.ok(ApiResponse.success("예약 취소 성공", responseDto));
    }
}
