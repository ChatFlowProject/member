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
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("friendship.uk_friendship_from_to"))
                throw new IllegalArgumentException("이미 생성된 친구관계입니다.");
        }
    }

    @Transactional
    public void acceptFriendship(Long friendshipId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 친구 요청입니다."));
        if(friendship.isFriend())
            throw new IllegalArgumentException("이미 수락된 친구요청입니다.");
        friendship.acceptFriendship();
    }

}
