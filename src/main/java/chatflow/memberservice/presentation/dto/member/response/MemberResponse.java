package chatflow.memberservice.presentation.dto.member.response;

import java.util.List;
import java.util.UUID;

public record MemberResponse(
        UUID requester,
        List<MemberSimpleResponse> memberList
) {
    public static MemberResponse from(UUID requester, List<MemberSimpleResponse> memberList) {
        return new MemberResponse(requester, memberList);
    }
}
