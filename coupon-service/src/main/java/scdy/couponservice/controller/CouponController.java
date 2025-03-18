package scdy.couponservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scdy.couponservice.common.advice.ApiResponse;
import scdy.couponservice.dto.CouponRequestDto;
import scdy.couponservice.dto.CouponResponseDto;
import scdy.couponservice.dto.UserCouponRequestDto;
import scdy.couponservice.dto.UserCouponResponseDto;
import scdy.couponservice.service.CouponService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupons")
public class CouponController {

    private final CouponService couponService;

    @PostMapping()
    public ResponseEntity<ApiResponse<CouponResponseDto>> createCoupon(@RequestHeader("X-Authenticated-User") Long userId,
                                                                       @RequestHeader("X-User-Role") String role,
                                                                       @RequestBody CouponRequestDto dto) {

        CouponResponseDto couponResponseDto = couponService.createCoupon(dto, role);
        return ResponseEntity.ok(ApiResponse.success("쿠폰 생성 성공", couponResponseDto));
    }

    @PatchMapping()
    public ResponseEntity<ApiResponse<CouponResponseDto>> updateCoupon(@RequestHeader("X-Authenticated-User") Long userId,
                                                                       @RequestHeader("X-User-Role") String role,
                                                                       @RequestBody CouponRequestDto dto) {

        CouponResponseDto couponResponseDto = couponService.updateCoupon(dto, userId, role);
        return ResponseEntity.ok(ApiResponse.success("쿠폰 수정 성공", couponResponseDto));
    }

    @GetMapping("/getCouponById")
    public ResponseEntity<ApiResponse<CouponResponseDto>> getCouponById(@RequestBody CouponRequestDto dto) {

        CouponResponseDto couponResponseDto = couponService.getCouponById(dto);
        return ResponseEntity.ok(ApiResponse.success("쿠폰 조회 성공", couponResponseDto));
    }

    @DeleteMapping()
    public ResponseEntity<ApiResponse<Void>> deleteCoupon(@RequestHeader("X-User-Role") String role,
                                                          @RequestBody CouponRequestDto dto) {

        couponService.deleteCoupon(dto, role);
        return ResponseEntity.ok(ApiResponse.success("쿠폰 삭제 성공"));
    }

    @PostMapping("/userCoupons")
    public ResponseEntity<ApiResponse<UserCouponResponseDto>> issueCoupon(@RequestHeader("X-User-Role") String role,
                                                                          @RequestBody UserCouponRequestDto dto) {

        UserCouponResponseDto userCouponResponseDto = couponService.issueCoupon(dto, role);
        return ResponseEntity.ok(ApiResponse.success("유저 쿠폰 발급 성공", userCouponResponseDto));
    }

    @GetMapping("/userCoupons/getById")
    public ResponseEntity<ApiResponse<UserCouponResponseDto>> readCoupon(@RequestHeader("X-Authenticated-User") Long userId,
                                                                         @RequestHeader("X-User-Role") String role,
                                                                         @RequestBody UserCouponRequestDto dto) {

        UserCouponResponseDto userCouponResponseDto = couponService.readCoupon(dto, userId, role);
        return ResponseEntity.ok(ApiResponse.success("유저 쿠폰 조회 성공", userCouponResponseDto));
    }

    @GetMapping("/userCoupons/getByUserId/{userId}")
    public ResponseEntity<ApiResponse<List<UserCouponResponseDto>>> getUserCouponListByUserId(@RequestHeader("X-Authenticated-User") Long requestUserId,
                                                                                              @RequestHeader("X-User-Role") String role,
                                                                                              @PathVariable("userId") Long userId) {

        List<UserCouponResponseDto> userCouponResponseDtos = couponService.getUserCouponListByUserId(userId, requestUserId, role);
        return ResponseEntity.ok(ApiResponse.success("유저 쿠폰 목록 조회 성공", userCouponResponseDtos));
    }

    @PutMapping("/userCoupons")
    public ResponseEntity<ApiResponse<UserCouponResponseDto>> updateUserCoupon(@RequestHeader("X-Authenticated-User") Long userId,
                                                                               @RequestHeader("X-User-Role") String role,
                                                                               @RequestBody UserCouponRequestDto dto) {

        UserCouponResponseDto userCouponResponseDto = couponService.updateUserCoupon(dto, userId, role);
        return ResponseEntity.ok(ApiResponse.success("유저 쿠폰 수정 성공", userCouponResponseDto));
    }

    @DeleteMapping("/userCoupons")
    public ResponseEntity<ApiResponse<Void>> deleteUserCoupon(@RequestHeader("X-User-Role") String role,
                                                              @RequestBody UserCouponRequestDto dto) {

        couponService.deleteUserCoupon(dto, role);
        return ResponseEntity.noContent().build();
    }

}