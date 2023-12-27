package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JDBC - DataSource 사용, JDBC Util 사용
 */
@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryV1 {

    private final DataSource dataSource;
    public Member save(Member member) throws SQLException {
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
            log.error("db error",e);
            throw e;
        }finally {
            close(con,psmt,null);
        }
    }

    public Member findById(String memberId) throws SQLException {
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
            log.error("dbError",e);
            throw e;
        }finally {
            close(con,psmt,rs);
        }
    }

    public void update(String memberId, int money) throws SQLException {
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
            log.error("error",e);
            throw e;
        }finally {
            close(con,psmt,null);
        }

    }

    public void delete(String memberId)throws SQLException{
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
            log.error("error",e);
            throw e;
        }finally {
            close(con,psmt,null);
        }
    }


    //사용한 리소스 닫아주기

    private void close(Connection con, Statement stmt, ResultSet rs){

        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);

    }

    private Connection getConnection() throws SQLException {

        Connection con = dataSource.getConnection();
        log.info("get Connection = {} ", con);

        return con;
    }
}
