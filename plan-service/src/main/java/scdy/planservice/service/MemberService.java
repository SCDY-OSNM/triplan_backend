package scdy.planservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scdy.planservice.common.exceptions.BadRequestException;
import scdy.planservice.dto.MemberRequestDto;
import scdy.planservice.dto.MemberResponseDto;
import scdy.planservice.entity.Member;
import scdy.planservice.enums.MemberRole;
import scdy.planservice.repository.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private MemberRepository memberRepository;

    // 멤버 생성 (링크를 통해 초대를 받았을 때)
    @Transactional
    public MemberResponseDto createMember(MemberRequestDto memberRequestDto){
        Member member = Member.builder()
                .plan(memberRequestDto.getPlan())
                .userId(memberRequestDto.getUserId())
                .memberRole(memberRequestDto.getMemberRole())
                .build();
        memberRepository.save(member);

        return MemberResponseDto.from(member);
    }

    // 멤버 조회
    public List<MemberResponseDto> readPlanMember(Long planId){
        List<Member> memberList = memberRepository.findByPlanId(planId);

        return memberList.stream().map(MemberResponseDto::from).toList();
    }

    @Transactional
    public MemberResponseDto updateMember(Long memberId, MemberRole memberRole){
        Member member = memberRepository.findByIdOrElseThrow(memberId);
        List<Member> memberList = memberRepository.findByPlanId(member.getPlan().getPlanId());

        if(memberRole == MemberRole.LEADER &&
                memberList.stream().anyMatch(m -> m.getMemberRole() == MemberRole.LEADER)){
            throw new BadRequestException("리더가 이미 존재합니다.");
        }
        member.updateMember(memberRole);

        return MemberResponseDto.from(member);
    }
    // 멤버 권한 수정

    // 멤버 삭제
    @Transactional
    public MemberResponseDto deleteMember(Long memberId){
        Member member = memberRepository.findByIdOrElseThrow(memberId);
        memberRepository.deleteById(memberId);

        return MemberResponseDto.from(member);
    }
}
