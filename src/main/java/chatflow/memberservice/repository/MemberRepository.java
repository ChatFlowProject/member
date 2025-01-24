package chatflow.memberservice.repository;

import chatflow.memberservice.common.MemberType;
import chatflow.memberservice.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByEmail(String email);
    List<Member> findAllByType(MemberType type);
}
