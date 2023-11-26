package pl.bartlomiej.emailverifydemo.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.bartlomiej.emailverifydemo.exceptions.model.ExceptionPayloadModel;
import pl.bartlomiej.emailverifydemo.exceptions.global.GlobalRestControllerException;
import pl.bartlomiej.emailverifydemo.exceptions.registration.ExpiredTokenException;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler(GlobalRestControllerException.class)
    public ResponseEntity<ExceptionPayloadModel> handleGlobalRestControllerException(GlobalRestControllerException exception) {
        HttpStatus httpStatus = exception.getHttpStatus();
        String message = exception.getMessage();
        ExceptionPayloadModel payloadModel = new ExceptionPayloadModel(message, httpStatus);
        return ResponseEntity.status(httpStatus).body(payloadModel);
    }

    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<ExceptionPayloadModel> handleExpiredTokenException(ExpiredTokenException exception) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        String message = exception.getMessage();
        Date expirationTime = exception.getTokenExpirationTime(); //todo: expiration time is 1hour back -> FIX
        ExceptionPayloadModel payloadModel = new ExceptionPayloadModel(message, expirationTime, httpStatus);
        return ResponseEntity.status(httpStatus).body(payloadModel);
    }
}
