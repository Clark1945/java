package proj.java.spring.aop;

import java.util.InputMismatchException;

public class IDNotCorrectException extends InputMismatchException {
    private final String message;
    public IDNotCorrectException(String message){
        this.message=message;
    }
    @Override
    public String getMessage(){
        return message;
    }
}
