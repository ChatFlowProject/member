package chatflow.memberservice.repository;

import chatflow.memberservice.entity.friendship.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendshipRepository extends JpaRepository<Friendship,Long> {
    Optional<Friendship> findByFromMemberIdAndToMemberId(UUID fromMemberId, UUID toMemberId);
    Optional<Friendship> findByIdAndFromMemberId(Long id, UUID fromMemberId);
    Optional<Friendship> findByIdAndToMemberId(Long id, UUID toMemberId);
    List<Friendship> findByFromMemberIdAndIsFriendFalse(UUID fromMemberId);
    List<Friendship> findByToMemberIdAndIsFriendFalse(UUID toMemberId);

//    @Query("SELECT f.toMember FROM Friendship f WHERE f.fromMember.id = :memberId AND f.isFriend = true")
//    List<Member> findFriendsByMemberId(@Param("memberId") UUID memberId);
}
