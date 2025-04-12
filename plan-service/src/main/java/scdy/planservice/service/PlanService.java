package scdy.planservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scdy.planservice.client.UserClient;
import scdy.planservice.dto.*;
import scdy.planservice.entity.Member;
import scdy.planservice.entity.Plan;
import scdy.planservice.entity.PlanDetail;
import scdy.planservice.entity.PlanPlace;
import scdy.planservice.enums.MemberRole;
import scdy.planservice.enums.Place;
import scdy.planservice.exception.MemberNotFoundException;
import scdy.planservice.exception.NotAllowedAuthException;
import scdy.planservice.repository.MemberRepository;
import scdy.planservice.repository.PlanDetailRepository;
import scdy.planservice.repository.PlanPlaceRepository;
import scdy.planservice.repository.PlanRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanService {
    private final PlanRepository planRepository;
    private final PlanPlaceRepository planPlaceRepository;
    private final PlanDetailRepository planDetailRepository;
    private final MemberRepository memberRepository;
    private final ChatService chatService;

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
        // 현재 유저로 리더 생성
        memberService.createLeader(userId, plan.getPlanId());
        // 해당 플랜의 채팅 생성

        String roomId = UUID.randomUUID().toString();

        ChatRoomDto chatRoomDto = ChatRoomDto.builder()
                .roomId(roomId)
                .planId(plan.getPlanId())
                .planTitle(plan.getPlanTitle())
                .memberCount(1)
                .members(List.of(userId))
                .build();

        chatService.createRoom(chatRoomDto);

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
            throw new NotAllowedAuthException("멤버만 조회 가능한 플랜입니다.");
        }
        return PlanResponseDto.from(plan);
    }

    // 플랜 수정
    @Transactional
    public PlanResponseDto updatePlan(Long planId, PlanRequestDto planRequestDto, Long userId){
        Plan plan = planRepository.findByIdOrElseThrow(planId);

        if(memberRepository.findByUserIdPlanId(userId, planId).isEmpty()) {
            throw new NotAllowedAuthException("멤버만 수정이 가능합니다.");
        }

        String updatedTitle = planRequestDto.getPlanTitle() != null
                ? planRequestDto.getPlanTitle() : plan.getPlanTitle();
        LocalDate updatedStartAt = planRequestDto.getPlanStartAt() != null
                ? planRequestDto.getPlanStartAt() : plan.getPlanStartAt();
        LocalDate updatedEndAt = planRequestDto.getPlanEndAt() != null
                ? planRequestDto.getPlanEndAt() : plan.getPlanEndAt();
        List<Place> updatedPlanPlace = planRequestDto.getPlanPlace() != null
                ? planRequestDto.getPlanPlace() : plan.getPlanPlace();

        plan.updatePlan(updatedTitle, updatedStartAt, updatedEndAt, updatedPlanPlace);

        return PlanResponseDto.from(plan);
    }

    // 플랜 삭제
    @Transactional
    public PlanResponseDto deletePlan(Long planId, Long userId){
        Plan plan = planRepository.findByIdOrElseThrow(planId);
        PlanResponseDto responseDto = PlanResponseDto.from(plan);

        memberRepository.findByUserIdPlanId(userId, planId).orElseThrow(
                () -> new MemberNotFoundException("멤버가 존재하지 않습니다."));

        if(!checkLeader(userId, planId)) {
            throw new NotAllowedAuthException("리더만 삭제가 가능합니다.");
        }

        List<Member> members = memberRepository.findByPlanId(planId);
        if (!members.isEmpty()) {
            for (Member member : members) {
                memberRepository.deleteById(member.getMemberId());
            }
        }
        List<PlanDetail> planDetails = planDetailRepository.findByPlanId(planId);
        if (!planDetails.isEmpty()) {
            for (PlanDetail planDetail : planDetails) {
                planDetailRepository.deleteById(planDetail.getPlanDetailId());
            }
        }
        planRepository.deleteById(planId);
        return responseDto;
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

        if(!checkLeader(userId, planId)){
            throw new NotAllowedAuthException("리더만 전환이 가능합니다.");
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

    // 플랜 채팅방 단일 조회
    public ChatRoomDto getChatRoomById(String roomId){
        return chatService.getRoom(roomId);
    }

    private boolean checkAuth(Long userId, Long planId){
        Plan plan = planRepository.findByIdOrElseThrow(planId);
        if(plan.getIsPublic()){
            return true;
        }
        return memberRepository.findByUserIdPlanId(userId, planId).isPresent();
    }

    private boolean checkLeader(Long userId, Long planId){
        Member member = memberRepository.findByUserIdPlanId(userId, planId).orElseThrow(
                ()-> new MemberNotFoundException("멤버가 존재하지 않습니다."));
        System.out.println("Checking leader - userId: " + userId + ", planId: " + planId + ", role: " + member.getMemberRole());
        return member.getMemberRole() == MemberRole.LEADER;
    }

}
