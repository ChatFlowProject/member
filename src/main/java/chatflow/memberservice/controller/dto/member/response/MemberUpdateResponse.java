package chatflow.memberservice.controller.dto.member.response;

import chatflow.memberservice.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;

public record MemberUpdateResponse(
        @Schema(description = "회원 이름", example = "Jerry")
        String name,
        @Schema(description = "회원 생년월일", example = "2000-04-07")
        String birth,
        @Schema(description = "회원 아바타 이미지 url", example = "https://snowball-bucket.s3.ap-northeast-2.amazonaws.com/f41b6bb9-3jerry.png")
        String avatarUrl
) {
    public static MemberUpdateResponse from(Member member) {
        return new MemberUpdateResponse(
                member.getName(),
                member.getBirth().toString(),
                member.getAvatar()
        );
    }
}
