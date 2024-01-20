package proj.java.spring.aop;

public class IDNotCorrectException extends RuntimeException {
    private final String message;
    public IDNotCorrectException(String message){
        this.message=message;
    }
    @Override
    public String getMessage(){
        return message;
    }
}
