package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class UnCheckedAppTest {

    static class Controller{
        Service service = new Service();
        public void request() throws SQLException, ConnectException {
            service.logic();
        }
    }
    static class Service{
        NetworkClient networkClient = new NetworkClient();
        Repository repository = new Repository();

        //계속 던지게 된다...
        public void logic(){
            repository.call();
            networkClient.call();
        }

    }

    static class NetworkClient{
        public void call(){
            throw new RuntimeConnectException("Connect fail");
        }

    }

    static class Repository{
        public void call(){
            try {
                runSQL();
            } catch (SQLException e) {
                throw new RuntimeSQLException(e);
            }
        }

        public void runSQL()throws SQLException{
            throw new SQLException("ex");
        }
    }

    static class RuntimeConnectException extends RuntimeException{
        public RuntimeConnectException(String message) {
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException{
        public RuntimeSQLException(String message) {
            super(message);
        }

        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }

    @Test
    void unCheckedTest() throws Exception {
        //given
        Controller controller = new Controller();
        // when
        assertThatThrownBy(controller::request).isInstanceOf(Exception.class);
        //then

    }

    @Test
    void printEx() {
        Controller controller = new Controller();
        try {
            controller.request();
        } catch (Exception e) {
            log.error("ex", e);
        }
    }
}
