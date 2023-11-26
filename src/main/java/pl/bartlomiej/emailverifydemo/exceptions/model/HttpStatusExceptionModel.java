package pl.bartlomiej.emailverifydemo.exceptions.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class HttpStatusExceptionModel extends RuntimeException {
    private final HttpStatus httpStatus;

    public HttpStatusExceptionModel(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
