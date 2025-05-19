package chatflow.memberservice.presentation.dto.friendship.response;

import chatflow.memberservice.presentation.dto.member.response.MemberSimpleResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record FriendshipInfoResponse(
        @Schema(description = "친구 요청 id", example = "8")
        Long friendshipId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        @Schema(description = "친구 요청 날짜")
        LocalDateTime friendshipDateTime,
        @Schema(description = "친구 요청 정보")
        MemberSimpleResponse friendshipInfo
) {
}
