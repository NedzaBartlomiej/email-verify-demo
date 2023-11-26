package pl.bartlomiej.emailverifydemo.exceptions.global;

import org.springframework.http.HttpStatus;
import pl.bartlomiej.emailverifydemo.exceptions.model.HttpStatusExceptionModel;

public class ResourceNotFoundException extends HttpStatusExceptionModel {
    public ResourceNotFoundException() {
        super("Resource not found.", HttpStatus.NOT_FOUND);
    }
}
