package pl.bartlomiej.emailverifydemo.exceptions.registration;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import pl.bartlomiej.emailverifydemo.exceptions.model.HttpStatusExceptionModel;

import java.util.Date;

@Getter
public class ExpiredTokenException extends HttpStatusExceptionModel {
    private final Date tokenExpirationTime;
    public ExpiredTokenException(Date tokenExpirationTime) {
        super("This token is expired.", HttpStatus.CONFLICT);
        this.tokenExpirationTime = tokenExpirationTime;
    }
}
