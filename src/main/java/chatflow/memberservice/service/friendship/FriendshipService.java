package chatflow.memberservice.service.friendship;

import chatflow.memberservice.presentation.dto.friendship.request.FriendshipRequest;
import chatflow.memberservice.presentation.dto.friendship.response.FriendshipInfoResponse;
import chatflow.memberservice.presentation.dto.friendship.response.FriendshipResponse;
import chatflow.memberservice.presentation.dto.member.response.MemberSimpleResponse;
import chatflow.memberservice.domain.friendship.FriendRequestStatus;
import chatflow.memberservice.domain.friendship.Friendship;
import chatflow.memberservice.domain.member.Member;
import chatflow.memberservice.domain.member.MemberState;
import chatflow.memberservice.common.exception.ErrorCode;
import chatflow.memberservice.common.exception.custom.ServiceException;
import chatflow.memberservice.infrastructure.repository.friendship.FriendshipRepository;
import chatflow.memberservice.infrastructure.repository.member.MemberRepository;
import chatflow.memberservice.service.member.MemberService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
    public FriendshipResponse requestFriendshipByNickname(UUID memberId, FriendshipRequest request) {
        Member member = memberService.getMemberById(memberId);
        if (request.friendNickname().equals(member.getNickname())) { // 내 자신에게 요청한 경우
            return new FriendshipResponse(FriendRequestStatus.INVALID_REQUEST);
        }
        Member friend = memberRepository.findByNickname(request.friendNickname()).orElse(null);
        if (friend == null) { // 존재하지 않은 nickname으로 요청한 경우
            return new FriendshipResponse(FriendRequestStatus.INVALID_REQUEST);
        }

        // 두 레코드만 조회: (member → friend)와 (friend → member)
        List<Friendship> checkFriendships = friendshipRepository.findByFromMemberIdInAndToMemberIdIn(member.getId(), friend.getId());

        if (checkFriendships.isEmpty()) { // 레코드 없는 경우: friendship 레코드 생성
            List<Friendship> friendships = Arrays.asList(
                    Friendship.request(member, friend, true),
                    Friendship.request(friend, member, false));
            friendshipRepository.saveAll(friendships);
            return new FriendshipResponse(FriendRequestStatus.REQUEST_SUCCESS);
        }

        Map<UUID, Friendship> friendshipMap = checkFriendships.stream()
                .collect(Collectors.toMap(f -> f.getFromMember().getId(), f -> f));
        Friendship memberToFriend = friendshipMap.get(member.getId());
        Friendship friendToMember = friendshipMap.get(friend.getId());
        if (memberToFriend == null || friendToMember == null) {
            throw new ServiceException(ErrorCode.INVALID_FRIENDSHIP);
        }

        if (memberToFriend.isFriend() && friendToMember.isFriend()) { // 이미 친구 관계인 경우
            return new FriendshipResponse(FriendRequestStatus.ALREADY_FRIENDS);
        }

        if (!memberToFriend.isFriend()) { // 상대방이 친구 요청 먼저한 경우
            memberToFriend.acceptFriendship();
            return new FriendshipResponse(FriendRequestStatus.FRIENDSHIP_ESTABLISHED);
        }

        return new FriendshipResponse(FriendRequestStatus.REQUEST_SUCCESS); // 내가 이미 요청한 경우
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
                        MemberSimpleResponse.from(friendship.getFromMember())
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
                        MemberSimpleResponse.from(friendship.getToMember())
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FriendshipInfoResponse> getAllFriends(UUID memberId) {
        List<Friendship> friendships = friendshipRepository.findFriendsByMemberId(memberId);
        return friendships.stream()
                .map(friendship -> new FriendshipInfoResponse(
                        friendship.getId(), // findFriendsByMemberId 쿼리에 의해 정방향 or 역방향 모두 들어올 수 있음
                        friendship.getCreatedAt(),
                        MemberSimpleResponse.from(friendship.getToMember())
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FriendshipInfoResponse> getOnlineFriends(UUID memberId) {
        List<Friendship> friendships = friendshipRepository.findFriendsByMemberId(memberId);
        return friendships.stream()
                .filter(friendship -> friendship.getToMember().getState().equals(MemberState.ONLINE))
                .map(friendship -> new FriendshipInfoResponse(
                        friendship.getId(),
                        friendship.getCreatedAt(),
                        MemberSimpleResponse.from(friendship.getToMember())
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Boolean isFriend(UUID memberId, UUID friendId) {
        return friendshipRepository.findByFromMemberIdInAndToMemberIdIn(memberId, friendId)
                .stream()
                .filter(Friendship::isFriend)
                .count() == 2;
    }

    @Transactional
    public void refuseFriendship(UUID memberId, Long friendshipId) { // 역방향 데이터 friendshipId (false 데이터)
        Friendship receivedFriendship = friendshipRepository.findByIdAndFromMemberId(friendshipId, memberId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 친구 요청입니다."));
        if (receivedFriendship.isFriend())
            throw new IllegalArgumentException("이미 수락한 친구 요청입니다.");
        Friendship sentFriendship = friendshipRepository.findByFromMemberIdAndToMemberId(receivedFriendship.getToMember().getId(), memberId)
                .orElseThrow(() -> new EntityNotFoundException("친구 요청을 찾을 수 없습니다."));
        friendshipRepository.deleteAllInBatch(Arrays.asList(receivedFriendship, sentFriendship));
    }

    @Transactional
    public void cancelFriendship(UUID memberId, Long friendshipId) { // 역방향 데이터 friendshipId (false 데이터)
        Friendship receivedFriendship = friendshipRepository.findByIdAndToMemberId(friendshipId, memberId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 친구 요청입니다."));
        if (receivedFriendship.isFriend())
            throw new IllegalArgumentException("이미 수락된 친구 요청입니다.");
        Friendship sentFriendship = friendshipRepository.findByFromMemberIdAndToMemberId(memberId, receivedFriendship.getFromMember().getId())
                .orElseThrow(() -> new EntityNotFoundException("친구 요청을 찾을 수 없습니다."));
        friendshipRepository.deleteAllInBatch(Arrays.asList(receivedFriendship, sentFriendship));
    }

    @Transactional
    public void deleteFriendship(UUID memberId, Long friendshipId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 친구 관계입니다."));
        if (!friendship.getFromMember().getId().equals(memberId) && !friendship.getToMember().getId().equals(memberId))
            throw new IllegalArgumentException("잘못된 친구 삭제 요청입니다.");
        Friendship rvsFriendship = friendshipRepository.findByFromMemberIdAndToMemberId(friendship.getToMember().getId(), friendship.getFromMember().getId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 친구 관계입니다."));
        if (!friendship.isFriend() || !rvsFriendship.isFriend())
            throw new IllegalArgumentException("친구 요청 수락 대기중인 친구 관계입니다.");
        friendshipRepository.deleteAllInBatch(Arrays.asList(friendship, rvsFriendship));
    }

}
