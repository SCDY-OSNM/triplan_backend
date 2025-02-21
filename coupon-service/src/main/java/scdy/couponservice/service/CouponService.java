package scdy.couponservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scdy.couponservice.dto.CouponRequestDto;
import scdy.couponservice.dto.CouponResponseDto;
import scdy.couponservice.dto.UserCouponRequestDto;
import scdy.couponservice.dto.UserCouponResponseDto;
import scdy.couponservice.entity.Coupon;
import scdy.couponservice.entity.UserCoupon;
import scdy.couponservice.enums.CouponType;
import scdy.couponservice.exception.CouponAmountErrorException;
import scdy.couponservice.exception.CouponAmountNullException;
import scdy.couponservice.exception.PermissionNotfoundException;
import scdy.couponservice.repository.CouponRepository;
import scdy.couponservice.repository.UserCouponRepository;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    //create coupon
    //Only ADMIN can create coupon
    @Transactional
    public CouponResponseDto createCoupon(CouponRequestDto couponRequestDto, Long userId, String userRole) {

        //check user
        if(!checkPermission(userRole, null, null)){
            throw new PermissionNotfoundException("권한이 없는 사용자입니다.");
        }

        boolean isLimit = couponRequestDto.getCouponType().equals(CouponType.LIMITED);
        boolean isCouponAmountNull = couponRequestDto.getCouponAmount() == null;

        if(isLimit && isCouponAmountNull){
            throw new CouponAmountNullException("LIMIT 쿠폰은 쿠폰 개수가 필수입니다.");
        }

        Coupon coupon = Coupon.builder()
                .couponName(couponRequestDto.getCouponName())
                .couponCode(couponRequestDto.getCouponCode())
                .couponType(couponRequestDto.getCouponType())
                .discountType(couponRequestDto.getDiscountType())
                .issueDate(couponRequestDto.getIssueDate())
                .expiryDate(couponRequestDto.getExpiryDate())
                .available(couponRequestDto.getAvailable())
                .couponMinimum(couponRequestDto.getCouponMinimum())
                .couponMaximum(couponRequestDto.getCouponMaximum())
                .discountPrice(couponRequestDto.getDiscountPrice())
                .discountPercentage(couponRequestDto.getDiscountPercentage())
                .couponAmount(couponRequestDto.getCouponAmount())
                .build();

        couponRepository.save(coupon);
        return CouponResponseDto.from(coupon);
    }


    //update coupon
    //Only ADMIN can update Coupon
    @Transactional
    public CouponResponseDto updateCoupon(CouponRequestDto couponRequestDto, Long userId, String userRole) {
        Coupon coupon = getCoupon(couponRequestDto.getCouponId());

        if(!checkPermission(userRole, null, null)){
            throw new PermissionNotfoundException("권한이 없는 사용자입니다.");
        }

        String couponName = couponRequestDto.getCouponName() == null ? coupon.getCouponName() : couponRequestDto.getCouponName();
        LocalDate expiryDate = couponRequestDto.getExpiryDate() == null ? coupon.getExpiryDate() : couponRequestDto.getExpiryDate();
        Boolean available = couponRequestDto.getAvailable() == null ? coupon.getAvailable() : couponRequestDto.getAvailable();

        coupon.updateCoupon(couponName, expiryDate, available);

        return CouponResponseDto.from(coupon);
    }

    //read coupon
    public CouponResponseDto getCouponById(CouponRequestDto couponRequestDto){
        Coupon coupon = getCoupon(couponRequestDto.getCouponId());

        return CouponResponseDto.from(coupon);
    }

    //delete coupon
    //Only ADMIN can delete coupon
    @Transactional
    public void deleteCoupon(CouponRequestDto couponRequestDto, String userRole){
        Coupon coupon = getCoupon(couponRequestDto.getCouponId());

        if(!checkPermission(userRole, null, null)){
            throw new PermissionNotfoundException("권한이 없는 사용자입니다.");
        }

        couponRepository.delete(coupon);
    }


    //issue coupon (create UserCoupon)
    //Only Admin can issue "ALL" type coupon
    //every user can issue "LIMIT" type coupon
    //"LIMIT" type coupon can issue amount less than "couponAmount"
    @Transactional
    public UserCouponResponseDto issueCoupon(UserCouponRequestDto userCouponRequestDto, String userRole){
        Coupon coupon = getCoupon(userCouponRequestDto.getCouponId());

        if(!checkPermission(userRole, null, null)){
            throw new PermissionNotfoundException("쿠폰발급 권한이 없는 사용자입니다.");
        }

        //check amount
        if(coupon.getCouponAmount() <= userCouponRepository.getUserCouponCountByCouponId(coupon.getCouponId())){
            throw new CouponAmountErrorException("쿠폰이 소진되었습니다");
        }

        UserCoupon userCoupon = UserCoupon.builder()
                .userId(userCouponRequestDto.getUserId())
                .coupon(coupon)
                .isUsed(false)
                .build();

        userCouponRepository.save(userCoupon);
        return UserCouponResponseDto.from(userCoupon);
    }


    //read UserCoupon
    //Only Admin or owned user can read UserCoupon
    public UserCouponResponseDto readCoupon(UserCouponRequestDto userCouponRequestDto, Long userId, String userRole){
        UserCoupon userCoupon = getUserCoupon(userCouponRequestDto.getUserCouponId());

        if(!checkPermission(userRole, userCoupon.getUserId() ,userId)){
            throw new PermissionNotfoundException("조회 권한이 없는 사용자입니다.");
        }

        return UserCouponResponseDto.from(userCoupon);
    }


    //read UserCoupon List by User
    //Only Admin or Owned user can read UserCuponList
    public List<UserCouponResponseDto> getUserCouponListByUserId(Long userId, String userRole){

        if(!checkPermission(userRole, null ,null)){
            throw new PermissionNotfoundException("조회 권한이 없는 사용자입니다.");
        }

        List<UserCoupon> userCouponList = userCouponRepository.getUserCouponListByUserId(userId);

        return userCouponList.stream()
                .map(UserCouponResponseDto::from)
                .toList();
    }

    //update UserCoupon
    //쿠폰 사용 여부 수정
    //Only admin and owned user update userCoupon
    @Transactional
    public UserCouponResponseDto updateUserCoupon(UserCouponRequestDto userCouponRequestDto, Long userId, String userRole){
        UserCoupon usercoupon = userCouponRepository.findByIdOrElseThrow(userCouponRequestDto.getUserCouponId());

        if(!checkPermission(userRole, usercoupon.getUserId() ,userId)){
            throw new PermissionNotfoundException("유저 쿠폰 수정 권한이 없는 사용자입니다.");
        }

        usercoupon.updateUserCoupon(userCouponRequestDto.getIsUsed());
        return UserCouponResponseDto.from(usercoupon);
    }


    //delete UserCoupon
    //Only ADMIN can delete userCoupon
    @Transactional
    public void deleteUserCoupon(UserCouponRequestDto userCouponRequestDto, String userRole){
        UserCoupon userCoupon = getUserCoupon(userCouponRequestDto.getUserCouponId());

        if(!checkPermission(userRole, null, null)){
            throw new PermissionNotfoundException("유저 쿠폰 삭제 권한이 없는 사용자입니다.");
        }

        userCouponRepository.delete(userCoupon);
    }



    // Permission Check
    private boolean checkPermission(String userRole,Long couponOwnerId, Long requestUserId) {
        boolean isAdmin = userRole.equals("ADMIN");
        boolean isOwner = couponOwnerId.equals(requestUserId);

        return isAdmin || isOwner;
    }

    //FeignClient 호출


    //Entity 호출
    private Coupon getCoupon(Long couponId){
        return couponRepository.findByIdOrElseThrow(couponId);
    }

    private UserCoupon getUserCoupon(Long userCouponId){
        return userCouponRepository.findByIdOrElseThrow(userCouponId);
    }

}
