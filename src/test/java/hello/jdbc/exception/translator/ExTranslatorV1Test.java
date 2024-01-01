package hello.jdbc.exception.translator;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.ex.MyDbException;
import hello.jdbc.repository.ex.MyDuplicateKeyException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class ExTranslatorV1Test {

    Repository repository;
    Service service;

    @BeforeEach
    void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        repository = new Repository(dataSource);
        service = new Service(repository);
    }


    @Test
    void test() {
        // when
        service.create("myId");
        service.create("myId");
        //then

    }


    @Slf4j
    @RequiredArgsConstructor
    static class Service{
        private final Repository repository;

        public void create(String memberId){

            try {
                repository.save(new Member(memberId, 0));
            }catch (MyDuplicateKeyException e){
                log.info("키 중복, 복구시도");
                String retryId = generateNewId(memberId);
                repository.save(new Member(retryId, 0));
            }catch (MyDbException e){
                throw e;
            }
        }
        private String generateNewId(String memberId){
            return memberId + new Random().nextInt(10000);
        }
    }
    @RequiredArgsConstructor
    static class Repository{
        private final DataSource dataSource;

        public Member save(Member member) {
            String sql = "insert into member(member_id, money) values(?,?)";
            Connection con = null;
            PreparedStatement psmt = null;

            try {
                con = dataSource.getConnection();
                psmt = con.prepareStatement(sql);
                psmt.setString(1, member.getMemberId());
                psmt.setInt(2, member.getMoney());
                psmt.execute();

                return member;

            } catch (SQLException e) {
                //h2 DB 키중복 에러코드 23505 h2DB에 의존하는 코드
                if (e.getErrorCode() == 23505) {
                    throw new MyDuplicateKeyException(e);
                }
                throw new MyDbException(e);

            } finally {
                JdbcUtils.closeStatement(psmt);
                JdbcUtils.closeConnection(con);
            }
        }
    }
}
