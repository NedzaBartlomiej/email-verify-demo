package pl.bartlomiej.emailverifydemo.exceptions.user;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import pl.bartlomiej.emailverifydemo.exceptions.model.HttpStatusExceptionModel;

@Getter
public class UserExistsException extends HttpStatusExceptionModel {
    public UserExistsException() {
        super("User with provided email is already exist.", HttpStatus.CONFLICT);
    }
}
