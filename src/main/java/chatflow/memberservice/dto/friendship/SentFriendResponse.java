package chatflow.memberservice.dto.friendship;

import chatflow.memberservice.dto.member.response.MemberSimpleResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record SentFriendResponse(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        @Schema(description = "요청 보낸 날짜")
        LocalDateTime createdAt,
        @Schema(description = "내가 보낸 친구 요청")
        MemberSimpleResponse friendInfo
) {
}
