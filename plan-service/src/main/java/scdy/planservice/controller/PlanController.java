package scdy.planservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scdy.planservice.common.advice.ApiResponse;
import scdy.planservice.dto.PlanRequestDto;
import scdy.planservice.dto.PlanResponseDto;
import scdy.planservice.enums.Place;
import scdy.planservice.service.MemberService;
import scdy.planservice.service.PlanService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/plans")
public class PlanController {
    private final PlanService planService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<ApiResponse<PlanResponseDto>> createPlan(@RequestHeader("X-Authenticated-User") Long userId, @RequestBody PlanRequestDto planRequestDto){
        PlanResponseDto planResponseDto = planService.createPlan(planRequestDto, userId);
        return ResponseEntity.ok(ApiResponse.success("플랜 생성 완료", planResponseDto));
    }

    @GetMapping("/{planId}")
    public ResponseEntity<ApiResponse<PlanResponseDto>> readPlan(@RequestHeader("X-Authenticated-User") Long userId, @PathVariable Long planId){
        PlanResponseDto planResponseDto = planService.readPlan(planId, userId);
        return ResponseEntity.ok(ApiResponse.success("플랜 조회 완료", planResponseDto));
    }

    @PutMapping("/{planId}")
    public ResponseEntity<ApiResponse<PlanResponseDto>> updatePlan(@RequestHeader ("X-Authenticated-User") Long userId, @PathVariable Long planId, @RequestBody PlanRequestDto planRequestDto){
        PlanResponseDto planResponseDto = planService.updatePlan(planId, planRequestDto, userId);
        return ResponseEntity.ok(ApiResponse.success("플랜 수정 완료", planResponseDto));
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<ApiResponse<PlanResponseDto>> deletePlan(@RequestHeader ("X-Authenticated-User") Long userId, @PathVariable Long planId){
        PlanResponseDto planResponseDto = planService.deletePlan(planId, userId);
        return ResponseEntity.ok(ApiResponse.success("플랜 삭제 완료", planResponseDto));
    }

    @PatchMapping("/public/{planId}")
    public ResponseEntity<ApiResponse<PlanResponseDto>> updatePlanPublic(@RequestHeader ("X-Authenticated-User") Long userId, @PathVariable Long planId){
        PlanResponseDto planResponseDto = planService.updatePublic(userId, planId);
        return ResponseEntity.ok(ApiResponse.success("공개 / 비공개 전환 완료", planResponseDto));
    }

    @GetMapping("/planlist/{localname}")
    public ResponseEntity<ApiResponse<List<PlanResponseDto>>> getLocalPlanList(@PathVariable Place place){
        List<PlanResponseDto> planResponseDto = planService.readPlanByPlace(place);
        return ResponseEntity.ok(ApiResponse.success("지역 플랜 조회 완료", planResponseDto));
    }
    // 지역별 일정 목록 조회

    // 일정 공유 (url)
}
