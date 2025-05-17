package chatflow.memberservice.domain.repository;

import chatflow.memberservice.domain.model.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendshipRepository extends JpaRepository<Friendship,Long> {
    @Query("SELECT f FROM Friendship f JOIN FETCH f.fromMember JOIN FETCH f.toMember WHERE f.fromMember.id = :fromMemberId AND f.toMember.id = :toMemberId")
    Optional<Friendship> findByFromMemberIdAndToMemberId(@Param("fromMemberId") UUID fromMemberId, @Param("toMemberId") UUID toMemberId);

    @Query("SELECT f FROM Friendship f JOIN FETCH f.fromMember JOIN FETCH f.toMember WHERE f.id = :id AND f.fromMember.id = :fromMemberId")
    Optional<Friendship> findByIdAndFromMemberId(@Param("id") Long id, @Param("fromMemberId") UUID fromMemberId);

    @Query("SELECT f FROM Friendship f JOIN FETCH f.fromMember JOIN FETCH f.toMember WHERE f.id = :id AND f.toMember.id = :toMemberId")
    Optional<Friendship> findByIdAndToMemberId(@Param("id") Long id, @Param("toMemberId") UUID toMemberId);

    @Query("SELECT f FROM Friendship f JOIN FETCH f.fromMember JOIN FETCH f.toMember WHERE f.fromMember.id = :fromMemberId AND f.isFriend = false")
    List<Friendship> findByFromMemberIdAndIsFriendFalse(@Param("fromMemberId") UUID fromMemberId);

    @Query("SELECT f FROM Friendship f JOIN FETCH f.fromMember JOIN FETCH f.toMember WHERE f.toMember.id = :toMemberId AND f.isFriend = false")
    List<Friendship> findByToMemberIdAndIsFriendFalse(@Param("toMemberId") UUID toMemberId);

    @Query("SELECT DISTINCT f FROM Friendship f " +
            "JOIN FETCH f.fromMember " +
            "JOIN FETCH f.toMember " +
            "WHERE (f.fromMember.id = :member1Id AND f.toMember.id = :member2Id) " +
            "   OR (f.fromMember.id = :member2Id AND f.toMember.id = :member1Id)")
    List<Friendship> findByFromMemberIdInAndToMemberIdIn(
            @Param("member1Id") UUID member1Id,
            @Param("member2Id") UUID member2Id
    );

    @Query("SELECT f FROM Friendship f " +
            "JOIN Friendship sf ON f.toMember = sf.fromMember " +
            "WHERE f.fromMember.id = :memberId " +
            "AND f.fromMember = sf.toMember " +
            "AND f.toMember = sf.fromMember " +
            "AND f.isFriend = true " +
            "AND sf.isFriend = true")
    List<Friendship> findFriendsByMemberId(@Param("memberId") UUID memberId);
}
