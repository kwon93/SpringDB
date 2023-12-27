package hello.jdbc.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class ConnectionTest {


    @Test
    @DisplayName("커넥션 획득에 성공해야한다.")
    void test() throws Exception {
        //given
        Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection con2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        log.info("connection = {}, class = {}",con1, con1.getClass());
        log.info("connection = {}, class = {}",con1, con1.getClass());


        // when

        //then

    }

    @Test
    @DisplayName("DataSource 를 통해 항상 커넥션풀에서 커넥션 획득에 성공해야한다.")
    void test2() throws Exception {
        //given
        //dataSource를 생성하는 시점에서 URL, USERNAME, PW를 입력해주면된다. 커넥션 생성때 입력하지않아도됨.
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        // when
        useDataSource(dataSource);
        //then

    }


    @Test
    @DisplayName("HikariCP를 사용해 커넥션 풀링")
    void test3() throws Exception {
        //given
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);
        dataSource.setPoolName("myPool");

        useDataSource(dataSource);
        Thread.sleep(1000);
        // when

        //then

    }


    private void useDataSource(DataSource dataSource) throws SQLException {
        Connection con1 = dataSource.getConnection();
        Connection con2 = dataSource.getConnection();
        log.info("connection = {} class = {}",con1, con1.getClass());
        log.info("connection = {}",con2);
    }

}



