package chatflow.memberservice.repository;

import chatflow.memberservice.entity.friendship.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendshipRepository extends JpaRepository<Friendship,Long> {
    Optional<Friendship> findByFromMemberIdAndToMemberId(UUID fromMemberId, UUID toMemberId);
    Optional<Friendship> findByIdAndFromMemberId(Long id, UUID fromMemberId);
    Optional<Friendship> findByIdAndToMemberId(Long id, UUID toMemberId);
    List<Friendship> findByFromMemberIdAndIsFriendFalse(UUID fromMemberId);
    List<Friendship> findByToMemberIdAndIsFriendFalse(UUID toMemberId);

    @Query("SELECT f FROM Friendship f " +
            "JOIN Friendship sf ON f.toMember = sf.fromMember " +
            "WHERE f.fromMember.id = :memberId " +
            "AND f.fromMember = sf.toMember " +
            "AND f.toMember = sf.fromMember " +
            "AND f.isFriend = true " +
            "AND sf.isFriend = true")
    List<Friendship> findFriendsByMemberId(@Param("memberId") UUID memberId);
}
