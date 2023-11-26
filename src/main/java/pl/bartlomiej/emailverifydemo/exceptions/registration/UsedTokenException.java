package pl.bartlomiej.emailverifydemo.exceptions.registration;

import org.springframework.http.HttpStatus;
import pl.bartlomiej.emailverifydemo.exceptions.model.HttpStatusExceptionModel;

public class UsedTokenException extends HttpStatusExceptionModel {
    public UsedTokenException() {
        super("Your account is already verified.", HttpStatus.CONFLICT);
    }
}
