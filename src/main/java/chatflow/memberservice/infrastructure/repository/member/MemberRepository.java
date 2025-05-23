package chatflow.memberservice.infrastructure.repository.member;

import chatflow.memberservice.domain.member.Member;
import chatflow.memberservice.domain.member.MemberType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);
    List<Member> findAllByType(MemberType type);
    List<Member> findByIdIn(List<UUID> ids);
}
