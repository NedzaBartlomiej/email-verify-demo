package pl.bartlomiej.emailverifydemo.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;

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
}
