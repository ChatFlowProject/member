package chatflow.memberservice.repository;

import chatflow.memberservice.entity.friendship.Friendship;
import chatflow.memberservice.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendshipRepository extends JpaRepository<Friendship,Long> {
    Optional<Friendship> findByFromMemberAndToMember(Member fromMember, Member toMember);
    List<Friendship> findByFromMemberIdAndIsFriendFalse(UUID fromMemberId);
    List<Friendship> findByToMemberIdAndIsFriendFalse(UUID toMemberId);

//    @Query("SELECT f.toMember FROM Friendship f WHERE f.fromMember.id = :memberId AND f.isFriend = true")
//    List<Member> findFriendsByMemberId(@Param("memberId") UUID memberId);
}
