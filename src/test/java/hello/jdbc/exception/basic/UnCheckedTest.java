package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class UnCheckedTest {

    static class MyUnCheckedException extends RuntimeException{

        public MyUnCheckedException(String message) {
            super(message);
        }
    }

    static class Repository{
        //Throw 생략가능.
        public void call(){
            throw new MyUnCheckedException("unEx");
        }
    }

    static class Service{
        Repository repository = new Repository();

        /**
         * 필요한 경우 예외를 잡아서 처리하면 된다.
         */
        public void uncheckedCall(){
            try{
                repository.call();
            }catch (MyUnCheckedException e){
                log.error("RuntimeException, message = {}",e.getMessage(), e);
            }

        }

        /**
         * 예외를 잡지 않아도된다. 자연스럽게 상위로 넘어감
         */
        public void callThrow(){
            repository.call();
        }
    }


    @Test
    @DisplayName("UncheckedException catch확인.")
    void test() throws Exception {
        //given
        Service service = new Service();
        // when
        service.uncheckedCall();
    }


    @Test
    @DisplayName("Runtime Throw확인")
    void test2() throws Exception {
        //given
        Service service = new Service();
        // when
        Assertions.assertThatThrownBy(service::callThrow)
                .isInstanceOf(MyUnCheckedException.class);
    }
}
