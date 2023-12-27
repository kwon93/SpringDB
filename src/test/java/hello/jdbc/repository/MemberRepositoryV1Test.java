package hello.jdbc.repository;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import hello.jdbc.connection.ConnectionConst;
import hello.jdbc.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.NoSuchElementException;

import static hello.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MemberRepositoryV1Test {

    MemberRepositoryV1 memberRepositoryV1;

    @BeforeEach
    void setUp() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        //HikariPooling
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        memberRepositoryV1 = new MemberRepositoryV1(dataSource);
    }

    @Test
    @DisplayName("멤버 저장을 커넥션풀을 사용해 성공해야한다.")
    void test1() throws Exception {
        //given
        Member memberV0 = Member.builder()
                .memberId("memberV0")
                .money(1000)
                .build();

        // when
        Member saved = memberRepositoryV1.save(memberV0);

        //then
        assertThat(saved.getMemberId()).isEqualTo(memberV0.getMemberId());
    }


    @Test
    @DisplayName("멤버 조회를 JDBC를 사용해 성공해야한다.")
    void test2() throws Exception {
        //given
        final String memberID = "memberV0";

        // when
        Member memberV0 = memberRepositoryV1.findById(memberID);
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
        memberRepositoryV1.update(memberID,updateMoney);
        Member memberV0 = memberRepositoryV1.findById(memberID);
        //then
        assertThat(memberV0.getMoney()).isEqualTo(updateMoney);

    }

    @Test
    @DisplayName("멤버 삭제를 JDBC를 사용해 성공해야한다.")
    void test4() throws Exception {
        //given
        final String memberID = "memberV0";

        // when
        memberRepositoryV1.delete(memberID);


        //then

        assertThrows(NoSuchElementException.class, () ->
        { Member memberV0 = memberRepositoryV1.findById(memberID);});


    }
}