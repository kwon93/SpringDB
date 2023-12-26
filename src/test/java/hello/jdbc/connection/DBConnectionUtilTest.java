package hello.jdbc.connection;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class DBConnectionUtilTest {



    @Test
    @DisplayName("JDBC 연결에 성공해야한다.")
    void test1() throws Exception {
        //given
        Connection connection = DBConnectionUtil.getConnection();
        //then
        assertThat(connection).isNotNull();
    }
}