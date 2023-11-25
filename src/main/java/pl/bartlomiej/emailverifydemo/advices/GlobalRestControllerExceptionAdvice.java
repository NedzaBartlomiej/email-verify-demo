package pl.bartlomiej.emailverifydemo.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.bartlomiej.emailverifydemo.exceptions.ExceptionPayloadModel;
import pl.bartlomiej.emailverifydemo.exceptions.GlobalRestControllerException;

@RestControllerAdvice
public class GlobalRestControllerExceptionAdvice {
    @ExceptionHandler(GlobalRestControllerException.class)
    public ResponseEntity<ExceptionPayloadModel> globalRestControllerExceptionHandle(GlobalRestControllerException exception) {
        HttpStatus httpStatus = exception.getHttpStatus();
        String message = exception.getMessage();
        ExceptionPayloadModel payloadModel = new ExceptionPayloadModel(message, httpStatus);
        return ResponseEntity.status(httpStatus).body(payloadModel);
    }
}
