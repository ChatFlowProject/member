package chatflow.memberservice.dto.sign_up.response;

import chatflow.memberservice.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record SignUpResponse(
        @Schema(description = "회원 고유키", example = "98bd5bf6-848a-43d4-8683-205523c9e359")
        UUID id,
        @Schema(description = "회원 아이디", example = "jerry0339")
        String account,
        @Schema(description = "회원 이름", example = "Jerry")
        String name,
        @Schema(description = "회원 나이", example = "15")
        Integer age
) {
    public static SignUpResponse from(Member member) {
        return new SignUpResponse(
                member.getId(),
                member.getAccount(),
                member.getName(),
                member.getAge()
        );
    }
}
