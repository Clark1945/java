package proj.java.spring.reflection;

import java.util.InputMismatchException;

public class PersonException extends InputMismatchException {
    private final String message;
    public PersonException(String message){
        this.message=message;
    }
    @Override
    public String getMessage(){
        return message;
    }
}
