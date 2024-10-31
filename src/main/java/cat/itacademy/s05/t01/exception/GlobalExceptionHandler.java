package cat.itacademy.s05.t01.exception;

import cat.itacademy.s05.t01.exception.custom.InactiveGameException;
import cat.itacademy.s05.t01.exception.custom.InvalidParticipantActionException;
import cat.itacademy.s05.t01.exception.custom.NoGameFoundException;
import cat.itacademy.s05.t01.exception.custom.NoUserFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InactiveGameException.class)
    public Mono<ResponseEntity<String>> handleInactiveGameException(InactiveGameException e) {
        log.error("Error; game is over: {}", e.getMessage());
        return Mono.just(ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(e.getMessage()));
    }

    @ExceptionHandler(NoGameFoundException.class)
    public Mono<ResponseEntity<String>> handleNoGameFoundException(NoGameFoundException e) {
        log.error("Error; non existing game entry in database: {}", e.getMessage());
        return Mono.just(ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage()));
    }

    @ExceptionHandler(NoUserFoundException.class)
    public Mono<ResponseEntity<String>> handleNoUserFoundException(NoUserFoundException e) {
        log.error("Error; non existing user entry in database: {}", e.getMessage());
        return Mono.just(ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage()));
    }

    @ExceptionHandler(InvalidParticipantActionException.class)
    public Mono<ResponseEntity<String>> handleInvalidParticipantAction(InvalidParticipantActionException ex) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage()));
    }
}
