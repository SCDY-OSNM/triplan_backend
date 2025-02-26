package scdy.planservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scdy.planservice.dto.PlanDetailRequestDto;
import scdy.planservice.dto.PlanDetailResponseDto;
import scdy.planservice.entity.Plan;
import scdy.planservice.entity.PlanDetail;
import scdy.planservice.exception.NotAllowedAuthException;
import scdy.planservice.repository.MemberRepository;
import scdy.planservice.repository.PlanDetailRepository;
import scdy.planservice.repository.PlanRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlanDetailService {
    private final PlanDetailRepository planDetailRepository;
    private final PlanRepository planRepository;
    private final MemberRepository memberRepository;

    // 세부 일정 생성
    @Transactional
    public PlanDetailResponseDto createPlanDetail(Long planId, PlanDetailRequestDto planDetailRequestDto){
        planRepository.findByIdOrElseThrow(planId);
        Plan plan = planRepository.findByIdOrElseThrow(planId);
        PlanDetail planDetail = PlanDetail.builder()
                .plan(plan)
                .contentId(planDetailRequestDto.getContentId())
                .planDetailCategory(planDetailRequestDto.getPlanCategory())
                .planDetailName(planDetailRequestDto.getPlanDetailName())
                .planDetailMemo(planDetailRequestDto.getPlanDetailMemo())
                .planDetailCost(planDetailRequestDto.getPlanDetailCost())
                .planDetailTime(planDetailRequestDto.getPlanDetailTime())
                .planDetailTimeLine(planDetailRequestDto.getPlanDetailTimeLine())
                .planDetailDay(planDetailRequestDto.getPlanDetailDay())
                .build();
        planDetailRepository.save(planDetail);

        return PlanDetailResponseDto.from(planDetail);
    }

    // 세부 일정 조회
    public PlanDetailResponseDto readSinglePlanDetail(Long planDetailId){
        PlanDetail planDetail = planDetailRepository.findByPlanDetailIdOrElseThrow(planDetailId);
        return PlanDetailResponseDto.from(planDetail);
    }

    //일정별 세부 일정 조회
    public List<PlanDetailResponseDto> readPlanDetailByPlan(Long userId, Long planId){
        Plan plan = planRepository.findByIdOrElseThrow(planId);
        if(!checkAuth(userId, planId)) {
            throw new NotAllowedAuthException("조회 권한이 없습니다.");
        }
        List<PlanDetail> planDetailList = planDetailRepository.findByPlanId(planId);
        return planDetailList.stream().map(PlanDetailResponseDto::from).toList();
    }

    // 세부 일정 수정 (순서)
    @Transactional
    public PlanDetailResponseDto updatePlanDetail(Long userId, Long planDetailId, Integer planDetailtimeLine, Integer planDetailDay){
        PlanDetail planDetail = planDetailRepository.findByPlanDetailIdOrElseThrow(planDetailId);
        if(!isMember(userId, planDetail.getPlan().getPlanId())){
            throw new NotAllowedAuthException("멤버만 수정할 수 있습니다.");
        }
        planDetail.updatePlanDetailTimeLine(planDetailtimeLine, planDetailDay);

        return PlanDetailResponseDto.from(planDetail);
    }


    // 세부 일정 수정 (컨텐츠)
    @Transactional
    public PlanDetailResponseDto updatePlanDetailContent(Long userId, Long planDetailId, PlanDetailRequestDto planDetailRequestDto){
        PlanDetail planDetail = planDetailRepository.findByPlanDetailIdOrElseThrow(planDetailId);
        if(!isMember(userId, planDetail.getPlan().getPlanId())){
            throw new NotAllowedAuthException("멤버만 수정할 수 있습니다.");
        }
        planDetail.updatePlanDetail(planDetailRequestDto.getPlanDetailName(), planDetailRequestDto.getPlanDetailMemo(), planDetailRequestDto.getPlanDetailTime());
        return PlanDetailResponseDto.from(planDetail);
    }

    // 세부 일정 삭제
    @Transactional
    public PlanDetailResponseDto deletePlanDetail(Long userId, Long planDetailId){
        PlanDetail planDetail = planDetailRepository.findByPlanDetailIdOrElseThrow(planDetailId);
        if(memberRepository.findByUserIdPlanId(userId, planDetail.getPlan().getPlanId()).isEmpty()){
            throw new NotAllowedAuthException("멤버만 삭제할 수 있습니다");
        }
        planDetailRepository.deleteById(planDetailId);
        return PlanDetailResponseDto.from(planDetail);
    }

    private boolean checkAuth(Long userId, Long planId){
        Plan plan = planRepository.findByIdOrElseThrow(planId);
        if(plan.getIsPublic()){
            return true;
        }
        return memberRepository.findByUserIdPlanId(userId, planId).isPresent();
    }

    private Boolean isMember(Long userId, Long planId){
        return memberRepository.findByUserIdPlanId(userId, userId).isPresent();
    }
}
