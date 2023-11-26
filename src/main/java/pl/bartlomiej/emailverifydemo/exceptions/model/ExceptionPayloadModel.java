package pl.bartlomiej.emailverifydemo.exceptions.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Getter
public class ExceptionPayloadModel {

    private final String message;
    private final HttpStatus httpStatus;
    private final ZonedDateTime zonedDateTime;
    private final ZoneId zone = ZoneId.systemDefault();
    public ExceptionPayloadModel(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.zonedDateTime = ZonedDateTime.now(zone);
    }

    // ExpiredTokenException constructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date tokenExpirationTime;
    public ExceptionPayloadModel(String message, Date tokenExpirationTime, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.zonedDateTime = ZonedDateTime.now(zone);
        this.tokenExpirationTime = tokenExpirationTime;
    }
}
