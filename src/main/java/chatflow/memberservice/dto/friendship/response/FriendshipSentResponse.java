package chatflow.memberservice.dto.friendship.response;

import chatflow.memberservice.dto.member.response.MemberSimpleResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record FriendshipSentResponse(
        @Schema(description = "보낸 친구 요청 id, 친구 요청 취소시 사용", example = "8")
        Long sentFriendshipId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        @Schema(description = "요청 보낸 날짜")
        LocalDateTime sentDateTime,
        @Schema(description = "내가 보낸 친구 요청")
        MemberSimpleResponse sentFriendshipInfo
) {
}
