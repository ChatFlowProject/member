package chatflow.memberservice.service;

import chatflow.memberservice.dto.friendship.FriendshipRequest;
import chatflow.memberservice.dto.friendship.ReceivedFriendResponse;
import chatflow.memberservice.dto.friendship.SentFriendResponse;
import chatflow.memberservice.dto.member.response.MemberSimpleResponse;
import chatflow.memberservice.entity.friendship.Friendship;
import chatflow.memberservice.entity.member.Member;
import chatflow.memberservice.repository.FriendshipRepository;
import chatflow.memberservice.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FriendshipService {
    private final MemberService memberService;
    private final FriendshipRepository friendshipRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void requestFriendship(UUID memberId, FriendshipRequest request) {
        Member member = memberService.getMemberById(memberId);
        if (request.friendNickname().equals(member.getNickname()))
            throw new IllegalArgumentException("친구 추가 요청할 수 없는 대상입니다.");
        Member friend = memberRepository.findByNickname(request.friendNickname())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 친구의 닉네임입니다."));
        try {
            friendshipRepository.save(Friendship.request(member, friend, true));
            friendshipRepository.save(Friendship.request(friend, member, false));
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("friendship.uk_friendship_from_to"))
                throw new IllegalArgumentException("이미 생성된 친구관계입니다.");
        }
    }

    @Transactional
    public void acceptFriendship(UUID memberId, Long friendshipId) {
        Member member = memberService.getMemberById(memberId);
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 친구 요청입니다."));
        if (!friendship.getFromMember().equals(member)) // member 다른 경우
            throw new IllegalArgumentException("잘못된 친구 요청 수락입니다.");
        if (friendship.isFriend())
            throw new IllegalArgumentException("이미 수락된 친구 요청입니다.");
        friendship.acceptFriendship();
    }

    @Transactional(readOnly = true)
    public List<SentFriendResponse> getSentFriendRequests(UUID memberId) {
        List<Friendship> friendships = friendshipRepository.findByToMemberIdAndIsFriendFalse(memberId);
        return friendships.stream()
                .map(friendship -> new SentFriendResponse(
                        friendship.getCreatedAt(),
                        new MemberSimpleResponse(
                                friendship.getFromMember().getNickname(),
                                friendship.getFromMember().getName(),
                                friendship.getFromMember().getCreatedAt()
                        )
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReceivedFriendResponse> getReceivedFriendRequests(UUID memberId) {
        List<Friendship> friendships = friendshipRepository.findByFromMemberIdAndIsFriendFalse(memberId);
        return friendships.stream()
                .map(friendship -> new ReceivedFriendResponse(
                        friendship.getId(),
                        friendship.getCreatedAt(),
                        new MemberSimpleResponse(
                                friendship.getToMember().getNickname(),
                                friendship.getToMember().getName(),
                                friendship.getToMember().getCreatedAt()
                        )
                ))
                .collect(Collectors.toList());
    }

//    @Transactional(readOnly = true)
//    public List<Member> getReceivedFriendRequests(UUID memberId) {
//        return friendshipRepository.findByToMemberIdAndIsFriendFalse(memberId)
//                .stream()
//                .map(Friendship::getFromMember)
//                .collect(Collectors.toList());
//    }

//    @Transactional(readOnly = true)
//    public List<Member> getFriends(UUID memberId) {
//        return friendshipRepository.findFriendsByMemberId(memberId);
//    }
}
