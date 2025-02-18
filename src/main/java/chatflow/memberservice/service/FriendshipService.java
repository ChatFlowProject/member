package chatflow.memberservice.service;

import chatflow.memberservice.dto.friendship.FriendshipRequest;
import chatflow.memberservice.entity.Friendship;
import chatflow.memberservice.entity.Member;
import chatflow.memberservice.repository.FriendshipRepository;
import chatflow.memberservice.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void requestFriendship(FriendshipRequest request) {
        Member member = memberRepository.findByNickname(request.memberNickname())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원의 닉네임입니다."));
        Member friend = memberRepository.findByNickname(request.friendNickname())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 친구의 닉네임입니다."));
        try {
            friendshipRepository.save(Friendship.request(member, friend, true));
            friendshipRepository.save(Friendship.request(friend, member, false));
            friendshipRepository.flush();
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("friendship.uk_friendship_from_to"))
                throw new IllegalArgumentException("이미 생성된 친구관계입니다.");
//            if(e.getMessage().contains(request.friendNickname()))
//                throw new IllegalArgumentException("이미 생성된 친구관계입니다.");
        }
    }

    public List<Friendship> getAllFriendships(UUID memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        return friendshipRepository.findByFromMemberOrToMember(member, member);
    }

    public List<Friendship> getAcceptedFriendships(UUID memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        List<Friendship> fromFriendships = friendshipRepository.findByFromMemberAndIsFriendTrue(member);
        List<Friendship> toFriendships = friendshipRepository.findByToMemberAndIsFriendTrue(member);

        List<Friendship> allFriendships = new ArrayList<>();
        allFriendships.addAll(fromFriendships);
        allFriendships.addAll(toFriendships);
        return allFriendships;
    }

    public List<Member> getFriendList(UUID memberId) {
        List<Friendship> friendships = getAcceptedFriendships(memberId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        return friendships.stream()
                .map(friendship -> friendship.getFromMember().equals(member)
                        ? friendship.getToMember()
                        : friendship.getFromMember())
                .collect(Collectors.toList());
    }
}
