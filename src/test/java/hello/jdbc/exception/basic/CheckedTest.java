package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class CheckedTest {
    
    static class MyCheckedException extends Exception{

        public MyCheckedException(String message) {
            super(message);
        }
    }


    @Test
    @DisplayName("CheckedException 확인")
    void test() throws Exception {
        //given
        Service service = new Service();

        // when
        service.callCatch();
        //then

    }


    @Test
    @DisplayName("CheckedThrow")
    void test2() throws Exception {
        //given
        Service service = new Service();

        //when
        Assertions.assertThrows(MyCheckedException.class, service::checkedThrow);

        //then

    }

    /**
     * CheckedException은 잡거나 던지거나 둘 중 하나를 선택해야함.
     */
    static class Service{
        Repository repository = new Repository();

        /*
        에외를 잡아서 처리하는 코드
         */
        public void callCatch(){
            try {
                repository.call();
            } catch (MyCheckedException e) {
                log.error("예외처리, message = {}",e.getMessage(), e);
            }
        }

        public void checkedThrow() throws MyCheckedException {
            repository.call();
        }
    }

    static class Repository{
        //Checked Exception은 Catch하거나 throw를 해줘야한다. 컴파일러가 Check하기 때문에,
        public void call()throws MyCheckedException{
            throw new MyCheckedException("ex");
        }
    }
}
