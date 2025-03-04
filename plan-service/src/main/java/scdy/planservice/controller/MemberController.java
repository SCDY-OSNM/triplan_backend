package scdy.planservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scdy.planservice.common.advice.ApiResponse;
import scdy.planservice.dto.MemberRequestDto;
import scdy.planservice.dto.MemberResponseDto;
import scdy.planservice.service.MemberService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<ApiResponse<MemberResponseDto>> createMember(@RequestHeader("X-Authenticated-User") Long userId, @RequestBody MemberRequestDto memberRequestDto){
        MemberResponseDto memberResponseDto = memberService.createMember(memberRequestDto);
        return ResponseEntity.ok(ApiResponse.success("멤버 생성 완료", memberResponseDto));
    }

    @GetMapping("/{planId}")
    public ResponseEntity<ApiResponse<List<MemberResponseDto>>> getMember(@RequestHeader("X-Authenticated-User") Long userId){
        List<MemberResponseDto> memberResponseDtoList = memberService.readPlanMember(userId);
        return ResponseEntity.ok(ApiResponse.success("플랜 멤버 전체 조회 완료", memberResponseDtoList));
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<ApiResponse<MemberResponseDto>> updateMemberRole(@RequestHeader("X-Authenticated-User") Long userId, @PathVariable Long memberId, Long planId){
        MemberResponseDto memberResponseDto = memberService.updateMember(userId, memberId, planId);
        return ResponseEntity.ok(ApiResponse.success("멤버 역할 수정 완료", memberResponseDto));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<ApiResponse<MemberResponseDto>> deleteMember(@RequestHeader("X-Authenticated-User") Long userId, @PathVariable Long memberId){
        MemberResponseDto memberResponseDto = memberService.deleteMember(userId, memberId);
        return ResponseEntity.ok(ApiResponse.success("멤버 삭제 완료", memberResponseDto));
    }
}
