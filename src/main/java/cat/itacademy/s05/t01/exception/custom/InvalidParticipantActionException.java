package cat.itacademy.s05.t01.exception.custom;

public class InvalidParticipantActionException extends RuntimeException {
    public InvalidParticipantActionException(String message) {
        super(message);
    }
}
