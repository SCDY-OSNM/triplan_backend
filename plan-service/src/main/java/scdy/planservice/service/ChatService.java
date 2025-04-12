package scdy.planservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import scdy.planservice.dto.ChatRoomDto;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String ROOM_PREFIX = "chat:room:";
    private static final String MEMBERS_PREFIX = "chat:members:";
    private static final String PLAN_PREFIX = "chat:plan:";


    // 채팅방 생성
    public void createRoom(ChatRoomDto room) {
        //ID기준 저장
        redisTemplate.opsForValue().set(ROOM_PREFIX + room.getPlanId(), room.getRoomId());
        // planId 매핑 저장
        redisTemplate.opsForValue().set(PLAN_PREFIX + room.getPlanId(), room.getRoomId());

    }

    // 채팅방 조회
    public ChatRoomDto getRoom(String roomId) {
        return (ChatRoomDto) redisTemplate.opsForValue().get(ROOM_PREFIX + roomId);
    }

    // 채팅방 멤버 추가
    public void addMember(String roomId, Long userId) {
        redisTemplate.opsForSet().add(MEMBERS_PREFIX + roomId, userId);
        ChatRoomDto room = getRoom(roomId);
        if (room != null) {
            room.setMemberCount(room.getMemberCount() + 1);
            room.getMembers().add(userId);
            redisTemplate.opsForValue().set(ROOM_PREFIX + roomId, room);
        }
    }

    // 채팅방 멤버 제거
    public void removeMember(String roomId, Long userId) {
        redisTemplate.opsForSet().remove(MEMBERS_PREFIX + roomId, userId);
        ChatRoomDto room = getRoom(roomId);
        if (room != null) {
            room.setMemberCount(room.getMemberCount() - 1);
            room.getMembers().remove(userId);
            redisTemplate.opsForValue().set(ROOM_PREFIX + roomId, room);
        }
    }
    
    public String findRoomIdByPlanId(Long planId){
        return (String) redisTemplate.opsForValue().get(PLAN_PREFIX + planId);
    }

    public boolean isMember(String roomId, Long userId) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(MEMBERS_PREFIX + roomId, userId));
    }
}
