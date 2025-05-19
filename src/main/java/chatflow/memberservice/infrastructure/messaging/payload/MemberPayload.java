package chatflow.memberservice.infrastructure.messaging.payload;

import chatflow.memberservice.domain.member.Member;
import chatflow.memberservice.domain.member.MemberState;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public record MemberPayload(
        UUID id,
        String nickname,
        String name,
        String avatarUrl,
        MemberState state,
        LocalDateTime createdAt
) {
    public static MemberPayload from(Member member) {
        return new MemberPayload(
                member.getId(),
                member.getNickname(),
                member.getName(),
                member.getAvatar(),
                member.getState(),
                member.getCreatedAt()
        );
    }
}
