package chatflow.memberservice.domain.model.friendship;

import chatflow.memberservice.domain.model.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_friendship_from_to",
                        columnNames = {
                                "from_member_id",
                                "to_member_id"
                        }
                )
        }
)
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_member_id", nullable = false)
    private Member fromMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member_id", nullable = false)
    private Member toMember;

    @Column(nullable = false)
    private boolean isFriend;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    private Friendship(Member fromMember, Member toMember, boolean isFriend) {
        this.fromMember = fromMember;
        this.toMember = toMember;
        this.isFriend = isFriend;
    }

    public static Friendship request(Member from, Member to, boolean isFriend) {
        return Friendship.builder()
                .fromMember(from)
                .toMember(to)
                .isFriend(isFriend)
                .build();
    }

    public void acceptFriendship() {
        this.isFriend = true;
        this.createdAt = LocalDateTime.now();
    }
}
