package chatflow.memberservice.repository;

import chatflow.memberservice.entity.Friendship;
import chatflow.memberservice.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship,Long> {
    List<Friendship> findByFromMemberOrToMember(Member fromMember, Member toMember);
    List<Friendship> findByFromMemberAndIsFriendTrue(Member fromMember);
    List<Friendship> findByToMemberAndIsFriendTrue(Member toMember);

}
