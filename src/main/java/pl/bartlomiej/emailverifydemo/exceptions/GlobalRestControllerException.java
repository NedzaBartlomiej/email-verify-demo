package pl.bartlomiej.emailverifydemo.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalRestControllerException extends RuntimeException {
    private final HttpStatus httpStatus;
    public GlobalRestControllerException(RuntimeException thrownException, HttpStatus httpStatus) {
        super(thrownException.getMessage());
        this.httpStatus = httpStatus;
    }
}
