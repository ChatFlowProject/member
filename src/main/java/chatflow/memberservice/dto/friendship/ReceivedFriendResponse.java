package chatflow.memberservice.dto.friendship;

import chatflow.memberservice.dto.member.response.MemberSimpleResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;


public record ReceivedFriendResponse(
        @Schema(description = "받은 친구 요청 id, 친구 수락시 사용", example = "9")
        Long friendshipId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        @Schema(description = "요청 받은 날짜")
        LocalDateTime createdAt,
        @Schema(description = "받은 친구 요청")
        MemberSimpleResponse friendInfo
) {
}
