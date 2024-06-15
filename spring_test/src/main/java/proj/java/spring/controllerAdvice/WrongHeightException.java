package proj.java.spring.controllerAdvice;

public class WrongHeightException extends RuntimeException {
    private final String message;

    public WrongHeightException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
