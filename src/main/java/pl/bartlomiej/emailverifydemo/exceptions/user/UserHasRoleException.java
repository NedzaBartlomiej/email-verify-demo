package pl.bartlomiej.emailverifydemo.exceptions.user;

import org.springframework.http.HttpStatus;
import pl.bartlomiej.emailverifydemo.exceptions.model.HttpStatusExceptionModel;

public class UserHasRoleException extends HttpStatusExceptionModel {
    public UserHasRoleException(String assignedRole) {
        super("User already have " + assignedRole + " role.", HttpStatus.CONFLICT);
    }
}
