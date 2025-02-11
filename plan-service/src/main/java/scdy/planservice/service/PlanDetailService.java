package scdy.planservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scdy.planservice.common.exceptions.NotFoundException;
import scdy.planservice.dto.PlanDetailRequestDto;
import scdy.planservice.dto.PlanDetailResponseDto;
import scdy.planservice.entity.Plan;
import scdy.planservice.entity.PlanDetail;
import scdy.planservice.repository.PlanDetailRepository;
import scdy.planservice.repository.PlanRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlanDetailService {
    private final PlanDetailRepository planDetailRepository;
    private final PlanRepository planRepository;

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
    public List<PlanDetailResponseDto> readPlanDetailByPlan(Long planId){
        Plan plan = planRepository.findByIdOrElseThrow(planId);
        List<PlanDetail> planDetailList = planDetailRepository.findByPlanId(planId);

        return planDetailList.stream().map(PlanDetailResponseDto::from).toList();
    } // repository 쿼리 DSL 적용 필요

    // 세부 일정 수정 (순서)
    @Transactional
    public PlanDetailResponseDto updatePlanDetail(Long planId, Long planDetailId, Integer planDetailtimeLine, Integer planDetailDay){
        PlanDetail planDetail = planDetailRepository.findByPlanDetailIdOrElseThrow(planDetailId);
        planDetail.updatePlanDetailTimeLine(planDetailtimeLine, planDetailDay);

        return PlanDetailResponseDto.from(planDetail);
    } // 프론트에서 순서 보내주면 정리해서 저장


    // 세부 일정 수정 (컨텐츠)
    @Transactional
    public PlanDetailResponseDto updatePlanDetailContent(Long planDetailId, PlanDetailResponseDto planDetailResponseDto){
        PlanDetail planDetail = planDetailRepository.findByPlanDetailIdOrElseThrow(planDetailId);
        planDetail.updatePlanDetail(planDetail.getPlanDetailName(), planDetail.getPlanDetailMemo(), planDetail.getPlanDetailTime());
        return PlanDetailResponseDto.from(planDetail);
    }

    // 세부 일정 삭제
    @Transactional
    public PlanDetailResponseDto deletePlanDetail(Long planDetailId, Long userId){
        PlanDetail planDetail = planDetailRepository.findByPlanDetailIdOrElseThrow(planDetailId);

        planDetailRepository.deleteById(planDetailId);
        return PlanDetailResponseDto.from(planDetail);
    }
}
