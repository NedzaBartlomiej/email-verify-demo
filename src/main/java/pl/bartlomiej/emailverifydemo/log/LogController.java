package pl.bartlomiej.emailverifydemo.log;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @GetMapping
    public ResponseEntity<List<Log>> getLogs() {
        List<Log> logs = logService.getLogs();
        if (logs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(logs);
        }
    }
}
