package chatflow.memberservice.domain.friendship;

import chatflow.memberservice.domain.BaseEntity;
import chatflow.memberservice.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
public class Friendship extends BaseEntity {

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
    }

}
