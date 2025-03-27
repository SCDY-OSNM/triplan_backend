package scdy.planservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scdy.planservice.dto.MemberRequestDto;
import scdy.planservice.dto.MemberResponseDto;
import scdy.planservice.entity.Member;
import scdy.planservice.enums.MemberRole;
import scdy.planservice.exception.MemberNotFoundException;
import scdy.planservice.exception.NotAllowedAuthException;
import scdy.planservice.repository.MemberRepository;
import scdy.planservice.repository.PlanRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PlanRepository planRepository;

    // 리더 생성 (플랜을 처음 생성했을 때)
    @Transactional
    public MemberResponseDto createLeader(Long userId, Long planId){
        Member member = Member.builder()
                .planId(planId)
                .memberRole(MemberRole.LEADER)
                .userId(userId)
                .build();
        System.out.println("MemberRole before save: " + member.getMemberRole()); // 디버깅
        memberRepository.save(member);

        return MemberResponseDto.from(member);
    }

    // 멤버 생성 (링크를 통해 초대를 받았을 때)
    @Transactional
    public MemberResponseDto createMember(MemberRequestDto memberRequestDto){
        planRepository.findByIdOrElseThrow(memberRequestDto.getPlanId());
        memberRepository.findByUserIdPlanId(memberRequestDto.getUserId(), memberRequestDto.getPlanId())
                .ifPresent(existingMember -> {
                    throw new IllegalStateException("이미 가입된 플랜입니다.");
                });

        Member member = Member.builder()
                .planId(memberRequestDto.getPlanId())
                .userId(memberRequestDto.getUserId())
                .memberRole(MemberRole.MEMBER) // 멤버가 없을 경우 자동으로 리더, 리더 있으면 자동으로 멤버
                .build();
        memberRepository.save(member);

        return MemberResponseDto.from(member);
    }

    // 멤버 조회
    public List<MemberResponseDto> readPlanMemberByPlan(Long planId){
        List<Member> memberList = memberRepository.findByPlanId(planId);

        return memberList.stream().map(MemberResponseDto::from).toList();
    }

    // 멤버 권한 수정
    @Transactional
    public MemberResponseDto updateMember(Long userId, Long memberId, Long planId){ // 현재 멤버, 바꿀 멤버, 바꿀 역할
        Member member = memberRepository.findByIdOrElseThrow(memberId);
        Member currentMember = memberRepository.findByUserIdPlanId(userId, planId).orElseThrow(
                () -> new MemberNotFoundException("멤버가 존재하지 않습니다."));

        if(!(currentMember.getMemberRole() == MemberRole.LEADER)){
            throw new NotAllowedAuthException("리더만 수정할 수 있습니다.");
        } // 사용자가 현재 플랜의 리더인지 확인

        member.updateMember(MemberRole.LEADER); // 무조건 리더 권한

        currentMember.updateMember(MemberRole.MEMBER); // 현재 리더를 일반 멤버로 전환

        return MemberResponseDto.from(member);
    }

    // 멤버 삭제
    @Transactional
    public MemberResponseDto deleteMember(Long userId,Long memberId){
        Member member = memberRepository.findByIdOrElseThrow(memberId);
        if(!checkLeader(userId, member.getPlanId())){
            throw new NotAllowedAuthException("리더만 삭제할 수 있습니다.");
        }
        memberRepository.deleteById(memberId);

        return MemberResponseDto.from(member);
    }

    private boolean checkLeader(Long userId, Long planId) {
        return memberRepository.findByUserIdPlanId(userId, planId)
                .map(member -> member.getMemberRole() == MemberRole.LEADER)
                .orElse(false);
    }

}
