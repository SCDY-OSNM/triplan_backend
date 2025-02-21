package scdy.planservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scdy.planservice.common.exceptions.BadRequestException;
import scdy.planservice.common.exceptions.NotFoundException;
import scdy.planservice.dto.MemberRequestDto;
import scdy.planservice.dto.MemberResponseDto;
import scdy.planservice.entity.Member;
import scdy.planservice.entity.Plan;
import scdy.planservice.enums.MemberRole;
import scdy.planservice.repository.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private MemberRepository memberRepository;

    // 리더 생성 (플랜을 처음 생성했을 때)
    @Transactional
    public MemberResponseDto createLeader(Long userId, Plan plan){
        Member member = Member.builder()
                .plan(plan)
                .memberRole(MemberRole.LEADER)
                .userId(userId)
                .build();
        memberRepository.save(member);

        return MemberResponseDto.from(member);
    }

    // 멤버 생성 (링크를 통해 초대를 받았을 때)
    @Transactional
    public MemberResponseDto createMember(MemberRequestDto memberRequestDto){
        Member member = Member.builder()
                .plan(memberRequestDto.getPlan())
                .userId(memberRequestDto.getUserId())
                .memberRole(memberRequestDto.getMemberRole()) // 멤버가 없을 경우 자동으로 리더, 리더 있으면 자동으로 멤버
                .build();
        memberRepository.save(member);

        return MemberResponseDto.from(member);
    }

    // 멤버 조회
    public List<MemberResponseDto> readPlanMember(Long planId){
        List<Member> memberList = memberRepository.findByPlanId(planId);

        return memberList.stream().map(MemberResponseDto::from).toList();
    }

    // 멤버 권한 수정
    @Transactional
    public MemberResponseDto updateMember(Long userId, Long memberId, Long planId){ // 현재 멤버, 바꿀 멤버, 바꿀 역할
        Member member = memberRepository.findByIdOrElseThrow(memberId);
        List<Member> memberList = memberRepository.findByPlanId(member.getPlan().getPlanId());
        Member currentMember = memberRepository.findByUserIdPlanId(userId, planId).orElseThrow(
                () -> new NotFoundException("멤버가 존재하지 않습니다.")
        );

        if(!(currentMember.getMemberRole() == MemberRole.LEADER)){
            throw new BadRequestException("리더만 수정할 수 있습니다.");
        } // 사용자가 현재 플랜의 리더인지 확인

        /*
        if(memberRole == MemberRole.LEADER &&
                memberList.stream().anyMatch(m -> m.getMemberRole() == MemberRole.LEADER)){
            throw new BadRequestException("리더가 이미 존재합니다.");
        }*/
        member.updateMember(MemberRole.LEADER); // 무조건 리더로만

        currentMember.updateMember(MemberRole.MEMBER); // 현재 리더를 일반 멤버로 전환

        return MemberResponseDto.from(member);
    }  // **** 리더 양도 개념으로 생각해서 다시 짜야될 듯 ****

    // 멤버 삭제
    @Transactional
    public MemberResponseDto deleteMember(Long userId,Long memberId){
        Member member = memberRepository.findByIdOrElseThrow(memberId);
        if(!checkLeader(userId, memberId)){
            throw new BadRequestException("리더만 삭제할 수 있습니다.");
        }
        memberRepository.deleteById(memberId);

        return MemberResponseDto.from(member);
    }

    private boolean checkLeader(Long userId, Long planId){
        Member member = memberRepository.findByUserIdPlanId(userId, planId).orElseThrow(
                ()-> new NotFoundException("Member Not Found"));
        if (member.getMemberRole() == MemberRole.LEADER) {
            return true;
        }
        return false;
    } // 멤버 아이디를 받아오면 짱 쉽게 짜질 것 같은데

}
