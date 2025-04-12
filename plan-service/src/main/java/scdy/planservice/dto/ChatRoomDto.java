package scdy.planservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class ChatRoomDto implements Serializable {
    private String roomId;

    private Long planId;

    private String planTitle;

    private int memberCount;

    private List<Long> members;

    @Builder
    public ChatRoomDto(String roomId, Long planId, String planTitle, int memberCount, List<Long> members) {
        this.roomId = roomId;
        this.planId = planId;
        this.planTitle = planTitle;
        this.memberCount = memberCount;
        this.members = members;
    }
}
