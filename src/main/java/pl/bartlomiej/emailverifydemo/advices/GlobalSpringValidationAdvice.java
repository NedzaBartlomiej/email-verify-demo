package pl.bartlomiej.emailverifydemo.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.bartlomiej.emailverifydemo.exceptions.model.ExceptionPayloadModel;

import java.util.Objects;

@RestControllerAdvice
public class GlobalSpringValidationAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionPayloadModel> methodArgumentNotValidExceptionHandle(MethodArgumentNotValidException exception) {

        BindingResult bindingResult = exception.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        String message =
                Objects.requireNonNull(fieldError).getField()
                        + " "
                        + Objects.requireNonNull(fieldError).getDefaultMessage();
        ExceptionPayloadModel payloadModel = new ExceptionPayloadModel(message, httpStatus);

        return ResponseEntity.status(httpStatus).body(payloadModel);
    }
}
