package pl.bartlomiej.emailverifydemo.log;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bartlomiej.emailverifydemo.exceptions.global.GlobalRestControllerException;
import pl.bartlomiej.emailverifydemo.exceptions.global.NoContentException;

import java.util.List;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @GetMapping
    public ResponseEntity<List<Log>> getLogs() {
        try {
            List<Log> logs = logService.getLogs();
            return ResponseEntity.status(HttpStatus.OK).body(logs);
        } catch (NoContentException exception) {
            throw new GlobalRestControllerException(exception, exception.getHttpStatus());
        }
    }
}
