package scdy.planservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scdy.planservice.common.advice.ApiResponse;
import scdy.planservice.dto.PlanDetailRequestDto;
import scdy.planservice.dto.PlanDetailResponseDto;
import scdy.planservice.service.PlanDetailService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/plandetails")
public class PlanDetailController {
    private final PlanDetailService planDetailService;

    @PostMapping
    public ResponseEntity<ApiResponse<PlanDetailResponseDto>> createPlanDetail(@RequestHeader ("X-Authenticated-User") Long userId, @RequestBody PlanDetailRequestDto planDetailRequestDto){
        PlanDetailResponseDto planDetailResponseDto = planDetailService.createPlanDetail(userId, planDetailRequestDto);
        return ResponseEntity.ok(ApiResponse.success("플랜 디테일 생성 완료", planDetailResponseDto));
    }

    @GetMapping("/{planDetailId}")
    public ResponseEntity<ApiResponse<PlanDetailResponseDto>> readPlanDetail(@RequestHeader ("X-Authenticated-User") Long userId, @PathVariable Long planDetailId){
        PlanDetailResponseDto planDetailResponseDto = planDetailService.readSinglePlanDetail(planDetailId);
        return ResponseEntity.ok(ApiResponse.success("플랜 디테일 조회 완료", planDetailResponseDto));
    }

    @GetMapping("/planlist/{planId}")
    public ResponseEntity<ApiResponse<List<PlanDetailResponseDto>>> readPlanDetailsList(@RequestHeader ("X-Authenticated-User") Long userId, @PathVariable Long planId){
        List<PlanDetailResponseDto> planDetailResponseDtoList = planDetailService.readPlanDetailByPlan(userId, planId);
        return ResponseEntity.ok(ApiResponse.success( planId + " 플랜 디테일 리스트 조회 완료", planDetailResponseDtoList));
    }

    @PatchMapping("/{planDetailId}")
    public ResponseEntity<ApiResponse<PlanDetailResponseDto>> updatePlanDetailOrder(@RequestHeader ("X-Authenticated-User") Long userId, @PathVariable Long planDetailId, @RequestBody Integer planDetailTimeLine, Integer planDetailDay){
        PlanDetailResponseDto planDetailResponseDto = planDetailService.updatePlanDetail(userId, planDetailId, planDetailTimeLine, planDetailDay);
        return ResponseEntity.ok(ApiResponse.success("플랜 디테일 순서 수정 완료", planDetailResponseDto));
    }

    @PutMapping("/{planDetailId}")
    public ResponseEntity<ApiResponse<PlanDetailResponseDto>> updatePlanDetailContent(@RequestHeader ("X-Authenticated-User") Long userId, @PathVariable Long planDetailId, @RequestBody PlanDetailRequestDto planDetailRequestDto){
        PlanDetailResponseDto planDetailResponseDto = planDetailService.updatePlanDetailContent(userId, planDetailId, planDetailRequestDto);
        return ResponseEntity.ok(ApiResponse.success("플랜 디테일 컨텐츠 수정 완료", planDetailResponseDto));
    }

    @DeleteMapping("/{planDetailId}")
    public ResponseEntity<ApiResponse<PlanDetailResponseDto>> deletePlanDetail(@RequestHeader ("X-Authenticated-User") Long userId, @PathVariable Long planDetailId){
        PlanDetailResponseDto planDetailResponseDto = planDetailService.deletePlanDetail(userId, planDetailId);
        return ResponseEntity.ok(ApiResponse.success("플랜 디테일 삭제 완료", planDetailResponseDto));
    }

}
