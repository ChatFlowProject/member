package chatflow.memberservice.repository;

import chatflow.memberservice.entity.member.MemberType;
import chatflow.memberservice.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);
    List<Member> findAllByType(MemberType type);
}
