package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.ex.MyDbException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

import static org.springframework.jdbc.datasource.DataSourceUtils.releaseConnection;

/**
 * 예외 누수 문제 해결
 * 체크 예외를 런타임으로 변경
 * MemberRepository Interface 사용
 */
@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryV4 implements MemberRepository {

    private final DataSource dataSource;
    @Override
    public Member save(Member member) {
        String sql = "insert into member(member_id, money) values (?, ?)";

        Connection con = null;
        PreparedStatement psmt = null;

        try {
            con = getConnection();
            psmt = con.prepareStatement(sql);
            psmt.setString(1, member.getMemberId());
            psmt.setInt(2, member.getMoney());
            psmt.executeUpdate(); //쿼리 실행
            return member;

        } catch (SQLException e) {
            throw new MyDbException(e);
        }finally {
            close(con,psmt,null);
        }
    }

    @Override
    public Member findById(String memberId) {
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            psmt = con.prepareStatement(sql);
            psmt.setString(1,memberId);

            rs =  psmt.executeQuery();

            if (rs.next()){
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            }else {
                throw new NoSuchElementException("member not found memberId = "+memberId);
            }
        }catch (SQLException e){
            throw new MyDbException(e);
        }finally {
            close(con,psmt,rs);
        }
    }


    @Override
    public void update(String memberId, int money) {
        String sql = "update member set money =? where member_id = ?";

        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            psmt = con.prepareStatement(sql);
            psmt.setInt(1,money);
            psmt.setString(2,memberId);

            //query를 실행하고 영향받은 row 개수
            int resultSize = psmt.executeUpdate();
            log.info("resultSize = {}", resultSize);
        }catch (SQLException e){
            throw new MyDbException(e);
        }finally {
            close(con,psmt,null);
        }

    }


    @Override
    public void delete(String memberId) {
        String sql = "delete from member where member_id = ?";

        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            psmt = con.prepareStatement(sql);
            psmt.setString(1,memberId);

            psmt.executeUpdate();

        }catch (SQLException e){
            throw new MyDbException(e);
        }finally {
            close(con,psmt,null);
        }
    }


    //사용한 리소스 닫아주기

    private void close(Connection con, Statement stmt, ResultSet rs){
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        //트랜잭션 동기화를 사용하려면  DataSourceUtils 를 사용해야한다.
        releaseConnection(con,dataSource);
    }

    private Connection getConnection() throws SQLException {
        //트랜잭션 동기화를 사용하려면  DataSourceUtils 를 사용해야한다.
        Connection con = DataSourceUtils.getConnection(dataSource);
        log.info("get Connection = {} ", con);
        return con;
    }
}
