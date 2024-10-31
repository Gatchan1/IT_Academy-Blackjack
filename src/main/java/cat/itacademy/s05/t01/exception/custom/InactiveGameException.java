package cat.itacademy.s05.t01.exception.custom;

public class InactiveGameException extends RuntimeException {
    public InactiveGameException(String message) {
        super(message);
    }
}
