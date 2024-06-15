package proj.java.spring.common;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import proj.java.spring.controllerAdvice.NameNotCorrectException;
import proj.java.spring.controllerAdvice.StringCreator;
import proj.java.spring.controllerAdvice.WrongHeightException;
import proj.java.spring.aop.IDNotCorrectException;



@Component
public class TestService {

    private final StringCreator stringCreator;

    public TestService(StringCreator stringCreator) {
        this.stringCreator = stringCreator;
    }

    @Cacheable(cacheNames = "returnStr")
    public String getReturnStr(String input) {
        // 模擬耗時操作，例如查詢數據庫或進行複雜計算
        try {
            Thread.sleep(3000); // 模擬延遲
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Result for " + input;
    }

    public String ifValidateFailThrowException(int id, String name, int height) {
        if (id == 0) {
            throw new IDNotCorrectException("ID ERROR");
        }
        if (name.equals("")) {
            throw new NameNotCorrectException("Name ERROR");
        }
        if (height == 0) {
            throw new WrongHeightException("Height ERROR");
        }
        return "OK";
    }

}

