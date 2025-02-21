package scdy.planservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scdy.planservice.client.UserClient;
import scdy.planservice.common.exceptions.BadRequestException;
import scdy.planservice.common.exceptions.NotFoundException;
import scdy.planservice.dto.*;
import scdy.planservice.entity.Member;
import scdy.planservice.entity.Plan;
import scdy.planservice.entity.PlanPlace;
import scdy.planservice.enums.MemberRole;
import scdy.planservice.enums.Place;
import scdy.planservice.repository.MemberRepository;
import scdy.planservice.repository.PlanPlaceRepository;
import scdy.planservice.repository.PlanRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanService {
    private final PlanRepository planRepository;
    private final PlanPlaceRepository planPlaceRepository;
    private final MemberRepository memberRepository;

    private final MemberService memberService;

    private final UserClient userClient;


    // 플랜 생성
    @Transactional
    public PlanResponseDto createPlan(PlanRequestDto planRequestDto, Long userId){
        Plan plan = Plan.builder()
                .planTitle(planRequestDto.getPlanTitle())
                .planStartAt(planRequestDto.getPlanStartAt())
                .planEndAt(planRequestDto.getPlanEndAt())
                .isPublic(planRequestDto.getIsPublic())
                .userId(userId)
                .planPlace(planRequestDto.getPlanPlace())
                .build();
        planRepository.save(plan);

        memberService.createLeader(userId, plan); // 서비스 내에서 서비스를 호출해도 괜찮은지

        return PlanResponseDto.from(plan);
    }

    // 플랜 위치 생성 - PlanPlace 사용
    @Transactional
    public PlanPlaceResponseDto createPlanPlace(PlanPlaceRequestDto planPlaceRequestDto){
        PlanPlace planPlace = PlanPlace.builder()
                .planId(planPlaceRequestDto.getPlan().getPlanId())
                .place(planPlaceRequestDto.getPlace())
                .build();

        planPlaceRepository.save(planPlace);

        return PlanPlaceResponseDto.from(planPlace);
    }

    // 단일 플랜 조회
    public PlanResponseDto readPlan(Long planId, Long userId){
        Plan plan = planRepository.findByIdOrElseThrow(planId);
        if(!checkAuth(userId, planId)) {
            throw new BadRequestException("비공개 플랜입니다.");
        }
        return PlanResponseDto.from(plan);
    }

    // 플랜 수정
    @Transactional
    public PlanResponseDto updatePlan(Long planId, PlanRequestDto planRequestDto, Long userId){
        Plan plan = planRepository.findByIdOrElseThrow(planId);
        if(memberRepository.findByUserIdPlanId(planId, userId).isEmpty()) {
            throw new BadRequestException("수정 권한이 없습니다.");
        }
        plan.updatePlan(planRequestDto.getPlanTitle(), planRequestDto.getPlanStartAt(), planRequestDto.getPlanEndAt(), planRequestDto.getPlanPlace());

        return PlanResponseDto.from(plan);
    }

    // 플랜 삭제
    @Transactional
    public PlanResponseDto deletePlan(Long planId, Long userId){
        Plan plan = planRepository.findByIdOrElseThrow(planId);
        Member member = memberRepository.findByUserIdPlanId(userId, planId).orElseThrow(
                () -> new NotFoundException("Member Not Exist"));

        if(!checkLeader(userId, planId)) {
            throw new BadRequestException("리더만 삭제가 가능합니다.");
        }
        planRepository.deleteById(planId);
        return PlanResponseDto.from(plan);
    }

    // 현재 사용자 별 일정 목록 (공/비공 고려x)
    public List<PlanResponseDto> readMyPlan(Long userId){
        UserResponseDto user = userClient.getUserById(userId).getData();
        List<Plan> planList = planRepository.findByUserIdInMember(userId);
        return planList.stream().map(PlanResponseDto::from).toList();
    }

    // 공개, 비공개 전환
    @Transactional
    public PlanResponseDto updatePublic(Long userId, Long planId){
        Plan plan = planRepository.findByIdOrElseThrow(planId);

        if(checkLeader(userId, planId)){
            throw new BadRequestException("리더만 전환이 가능합니다.");
        }
        if(plan.getIsPublic()==Boolean.TRUE){
            plan.changePublic(Boolean.FALSE);
        }
        else if(plan.getIsPublic() == Boolean.FALSE){
            plan.changePublic(Boolean.TRUE);
        }
        return PlanResponseDto.from(plan);
    }

    // 지역별 일정 목록 - 속성값 List 사용
    public List<PlanResponseDto> readPlanByPlace(Place place){
        List<Plan> planList = planRepository.findByPlace(place);

        return planList.stream()
                .filter(plan-> !plan.getIsPublic()).map(PlanResponseDto::from).toList();
    }

    // 지역별 일정 목록 - PlanPlace 테이블 사용
    public List<PlanResponseDto> readPlanByPlace2(Place place){
        List<PlanPlace> planPlaceList = planPlaceRepository.findPlanPlaceByPlace(place);

        List<Plan> planList = planPlaceList.stream().map(planPlace -> planRepository.findByIdOrElseThrow(planPlace.getPlanId())).toList();

        return planList.stream()
                .filter(plan-> !plan.getIsPublic()).map(PlanResponseDto::from).toList();
    }

    // 일정 공유

    private boolean checkAuth(Long userId, Long planId){
        Plan plan = planRepository.findByIdOrElseThrow(planId);
        if(plan.getIsPublic()){
            return true;
        }
        return memberRepository.findByUserIdPlanId(userId, planId).isPresent();
    }

    private boolean checkLeader(Long userId, Long planId){
        Member member = memberRepository.findByUserIdPlanId(userId, planId).orElseThrow(
                ()-> new NotFoundException("Member Not Found"));
        if(member.getMemberRole()== MemberRole.LEADER) {
            return true;
        }
        return false;
    }

}
