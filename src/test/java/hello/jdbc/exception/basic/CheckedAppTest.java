package hello.jdbc.exception.basic;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;

public class CheckedAppTest {

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
        public void logic() throws SQLException, ConnectException {
            repository.call();
            networkClient.call();
        }

    }

    static class NetworkClient{
        public void call()throws ConnectException{
            throw new ConnectException("Connect fail");
        }

    }

    static class Repository{

        public void call()throws SQLException{
            throw new SQLException("ex");
        }
    }


    @Test
    void test1() throws Exception {
        //given
        Controller controller = new Controller();
        // when
        assertThatThrownBy(controller::request).isInstanceOf(Exception.class);
        //then

    }
}
