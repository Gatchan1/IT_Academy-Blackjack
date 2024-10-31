package cat.itacademy.s05.t01.exception.custom;

public class NoUserFoundException extends RuntimeException {
    public NoUserFoundException(String message) {
        super(message);
    }
}
