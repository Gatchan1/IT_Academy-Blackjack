package cat.itacademy.s05.t01.exception.custom;

public class NoGameFoundException extends RuntimeException {
    public NoGameFoundException(String message) {
         super(message);
    }
}
