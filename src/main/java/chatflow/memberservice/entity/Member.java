package chatflow.memberservice.entity;


import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "members")
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String memberId;
    @Column(nullable = false)
    private String encryptedPwd;
    @Column(nullable = false, length = 50, unique = true)
    private String email;
    @Column(nullable = false, length = 50)
    private String name;
    @Enumerated(EnumType.STRING)
    private MemberState memberState;
}
