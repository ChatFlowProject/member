package chatflow.memberservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum MemberState {
    ONLINE("온라인"),
    IDLE("자리비움"),
    DO_NOT_DISTURB("방해금지"),
    OFFLINE("오프라인");

    private String name;
}
