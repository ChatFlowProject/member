package chatflow.memberservice.service;

import chatflow.memberservice.dto.friendship.request.FriendshipRequest;
import chatflow.memberservice.dto.friendship.response.FriendshipInfoResponse;
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

    @Transactional(readOnly = true)
    public Friendship getFriendshipById(Long id) {
        return friendshipRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 친구 요청입니다."));
    }

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
                throw new IllegalArgumentException("이미 친구 요청한 회원입니다.");
        }
    }

    @Transactional
    public void acceptFriendship(UUID memberId, Long friendshipId) {
        Member member = memberService.getMemberById(memberId);
        Friendship friendship = getFriendshipById(friendshipId);
        if (!friendship.getFromMember().equals(member)) // member 다른 경우
            throw new IllegalArgumentException("잘못된 친구 요청 수락입니다.");
        if (friendship.isFriend())
            throw new IllegalArgumentException("이미 수락된 친구 요청입니다.");
        friendship.acceptFriendship(); // 수락시 역방향 데이터의 createdAt만 변경
    }

    @Transactional(readOnly = true)
    public List<FriendshipInfoResponse> getSentFriendRequests(UUID memberId) {
        List<Friendship> friendships = friendshipRepository.findByToMemberIdAndIsFriendFalse(memberId);
        return friendships.stream()
                .map(friendship -> new FriendshipInfoResponse(
                        friendship.getId(), // 역방향 데이터 friendshipId (false 데이터)
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
    public List<FriendshipInfoResponse> getReceivedFriendRequests(UUID memberId) {
        List<Friendship> friendships = friendshipRepository.findByFromMemberIdAndIsFriendFalse(memberId);
        return friendships.stream()
                .map(friendship -> new FriendshipInfoResponse(
                        friendship.getId(), // 역방향 데이터 friendshipId (false 데이터)
                        friendship.getCreatedAt(),
                        new MemberSimpleResponse(
                                friendship.getToMember().getNickname(),
                                friendship.getToMember().getName(),
                                friendship.getToMember().getCreatedAt()
                        )
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FriendshipInfoResponse> getAllFriends(UUID memberId) {
        List<Friendship> friendships = friendshipRepository.findFriendsByMemberId(memberId);
        return friendships.stream()
                .map(friendship -> new FriendshipInfoResponse(
                        friendship.getId(), // 역방향 데이터 friendshipId (false 데이터)
                        friendship.getCreatedAt(),
                        new MemberSimpleResponse(
                                friendship.getToMember().getNickname(),
                                friendship.getToMember().getName(),
                                friendship.getToMember().getCreatedAt()
                        )
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void refuseFriendship(UUID memberId, Long friendshipId) { // 역방향 데이터 friendshipId (false 데이터)
        Friendship receivedFriendship = friendshipRepository.findByIdAndFromMemberId(friendshipId, memberId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 친구 요청입니다."));
        if (receivedFriendship.isFriend())
            throw new IllegalArgumentException("이미 수락한 친구 요청입니다.");
        Friendship sentFriendship = friendshipRepository.findByFromMemberIdAndToMemberId(receivedFriendship.getToMember().getId(), memberId)
                .orElseThrow(() -> new EntityNotFoundException("친구 요청을 찾을 수 없습니다."));
        friendshipRepository.delete(receivedFriendship);
        friendshipRepository.delete(sentFriendship);
    }

    @Transactional
    public void cancelFriendship(UUID memberId, Long friendshipId) { // 역방향 데이터 friendshipId (false 데이터)
        Friendship receivedFriendship = friendshipRepository.findByIdAndToMemberId(friendshipId, memberId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 친구 요청입니다."));
        if (receivedFriendship.isFriend())
            throw new IllegalArgumentException("이미 수락된 친구 요청입니다.");
        Friendship sentFriendship = friendshipRepository.findByFromMemberIdAndToMemberId(memberId, receivedFriendship.getFromMember().getId())
                .orElseThrow(() -> new EntityNotFoundException("친구 요청을 찾을 수 없습니다."));
        friendshipRepository.delete(receivedFriendship);
        friendshipRepository.delete(sentFriendship);
    }

    @Transactional
    public void deleteFriendship(UUID memberId, Long friendshipId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 친구 관계입니다."));
        if(!friendship.getFromMember().getId().equals(memberId) && !friendship.getToMember().getId().equals(memberId))
            throw new IllegalArgumentException("잘못된 친구 삭제 요청입니다.");
        Friendship rvsFriendship = friendshipRepository.findByFromMemberIdAndToMemberId(friendship.getToMember().getId(), friendship.getFromMember().getId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 친구 관계입니다."));
        if (!friendship.isFriend() || !rvsFriendship.isFriend())
            throw new IllegalArgumentException("친구 요청 수락 대기중인 친구 관계입니다.");
        friendshipRepository.delete(friendship);
        friendshipRepository.delete(rvsFriendship);
    }
}
