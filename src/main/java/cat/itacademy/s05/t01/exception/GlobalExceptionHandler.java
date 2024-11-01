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
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InactiveGameException.class)
    public Mono<ResponseEntity<String>> handleInactiveGameException(InactiveGameException e) {
        log.error("Error. Game is over: {}", e.getMessage());
        return Mono.just(ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(e.getMessage()));
    }

    @ExceptionHandler(NoGameFoundException.class)
    public Mono<ResponseEntity<String>> handleNoGameFoundException(NoGameFoundException e) {
        log.error("Error. Non existing game entry in database: {}", e.getMessage());
        return Mono.just(ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage()));
    }

    @ExceptionHandler(NoUserFoundException.class)
    public Mono<ResponseEntity<String>> handleNoUserFoundException(NoUserFoundException e) {
        log.error("Error. Non existing user entry in database: {}", e.getMessage());
        return Mono.just(ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage()));
    }

    @ExceptionHandler(InvalidParticipantActionException.class)
    public Mono<ResponseEntity<String>> handleInvalidParticipantAction(InvalidParticipantActionException e) {
        log.error("Error. Invalid move name: {}", e.getMessage());
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage()));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<String>> handleWebExchangeBindException(WebExchangeBindException e) {
        log.error("Error. Validation failure: {}", e.getMessage());
        String errorMessage = e.getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return Mono.just(ResponseEntity
                .badRequest()
                .body("Validation failure: " + errorMessage));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<String>> handleGlobalException(Exception e) {
        log.error("Unexpected error: {}", e.getMessage());
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unexpected error: " + e.getMessage()));
    }
}
