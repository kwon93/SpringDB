package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberRepositoryV0Test {

    MemberRepositoryV0 memberRepositoryV0 = new MemberRepositoryV0();


    @Test
    @DisplayName("멤버 저장을 JDBC를 사용해 성공해야한다.")
    void test1() throws Exception {
        //given
        Member memberV0 = Member.builder()
                .memberId("memberV0")
                .money(1000)
                .build();

        // when
        Member saved = memberRepositoryV0.save(memberV0);

        //then
        assertThat(saved.getMemberId()).isEqualTo(memberV0.getMemberId());
    }


    @Test
    @DisplayName("멤버 조회를 JDBC를 사용해 성공해야한다.")
    void test2() throws Exception {
        //given
        final String memberID = "memberV0";

        // when
        Member memberV0 = memberRepositoryV0.findById(memberID);
        //then
        assertThat(memberV0.getMemberId()).isEqualTo(memberID);

    }

    @Test
    @DisplayName("멤버 수정을 JDBC를 사용해 성공해야한다.")
    void test3() throws Exception {
        //given
        final String memberID = "memberV0";
        final int updateMoney = 30000;

        // when
        memberRepositoryV0.update(memberID,updateMoney);
        Member memberV0 = memberRepositoryV0.findById(memberID);
        //then
        assertThat(memberV0.getMoney()).isEqualTo(updateMoney);

    }

    @Test
    @DisplayName("멤버 삭제를 JDBC를 사용해 성공해야한다.")
    void test4() throws Exception {
        //given
        final String memberID = "memberV0";

        // when
        memberRepositoryV0.delete(memberID);


        //then

        assertThrows(NoSuchElementException.class, () ->
        { Member memberV0 = memberRepositoryV0.findById(memberID);});


    }
}