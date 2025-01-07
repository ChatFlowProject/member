package chatflow.memberservice.dto;


import chatflow.memberservice.entity.MemberState;
import lombok.Data;

import java.util.Date;

@Data
public class MemberDto {
    private String memberId;
    private String encryptedPwd;
    private String email;
    private String name;
    private MemberState memberState;
    private String pwd;
    private Date createdAt;
}
