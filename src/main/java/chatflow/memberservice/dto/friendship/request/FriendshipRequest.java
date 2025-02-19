package chatflow.memberservice.dto.friendship.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FriendshipRequest(
        @NotBlank(message = "친구가 되고 싶은 회원의 닉네임을 입력해 주세요")
        @Size(min = 2, max = 20, message = "20자 이내로 입력해 주세요.")
        @Schema(description = "회원 닉네임", example = "tom9330")
        String friendNickname
) {
}
