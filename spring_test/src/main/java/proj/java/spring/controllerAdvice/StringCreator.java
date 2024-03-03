package proj.java.spring.controllerAdvice;

import org.springframework.stereotype.Component;

@Component
public class StringCreator {
    public String getDetailInformation(int id, String name, int height) {
        return "User " + name + " is " + height + " tall (ID=" + id + ")";
    }
}
