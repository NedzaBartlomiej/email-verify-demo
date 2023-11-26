package pl.bartlomiej.emailverifydemo.exceptions.global;

import org.springframework.http.HttpStatus;
import pl.bartlomiej.emailverifydemo.exceptions.model.HttpStatusExceptionModel;

public class NoContentException extends HttpStatusExceptionModel {
    public NoContentException() {
        super("No content to return.", HttpStatus.NO_CONTENT);
    }
}
