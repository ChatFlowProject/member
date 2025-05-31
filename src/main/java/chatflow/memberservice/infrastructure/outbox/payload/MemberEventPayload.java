package chatflow.memberservice.infrastructure.outbox.payload;

import chatflow.memberservice.domain.member.Member;
import chatflow.memberservice.domain.member.MemberState;

import java.time.LocalDateTime;
import java.util.UUID;

public record MemberEventPayload(
        UUID id,
        String nickname,
        String name,
        String avatarUrl,
        MemberState state,
        LocalDateTime createdAt,
        LocalDateTime timestamp // 이벤트 발행 시간 - 오래된 이벤트 처리를 무시하기 위해 필요
) {
    public static MemberEventPayload from(Member member) {
        return new MemberEventPayload(
                member.getId(),
                member.getNickname(),
                member.getName(),
                member.getAvatarUrl(),
                member.getState(),
                member.getCreatedAt(),
                LocalDateTime.now()
        );
    }
}
