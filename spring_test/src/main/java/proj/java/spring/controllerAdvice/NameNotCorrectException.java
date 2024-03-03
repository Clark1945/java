package proj.java.spring.controllerAdvice;

public class NameNotCorrectException extends RuntimeException{
    private final String message;
    public NameNotCorrectException(String message){
        this.message=message;
    }
    @Override
    public String getMessage(){
        return message;
    }
}
