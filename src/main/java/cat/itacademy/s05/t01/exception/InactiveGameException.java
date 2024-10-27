package cat.itacademy.s05.t01.exception;

public class InactiveGameException extends RuntimeException {
    public InactiveGameException(String message) {
        super(message);
    }
}
