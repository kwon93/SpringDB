package hello.jdbc.domain;

import lombok.Builder;
import lombok.Data;

@Data
public class Member {

    private String memberId;
    private int money;

    public Member() {
    }

    @Builder
    public Member(String memberId, int money) {
        this.memberId = memberId;
        this.money = money;
    }
}
