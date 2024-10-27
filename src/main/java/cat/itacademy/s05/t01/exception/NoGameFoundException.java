package cat.itacademy.s05.t01.exception;

public class NoGameFoundException extends RuntimeException {
    public NoGameFoundException(String message) {
         super(message);
    }
}
