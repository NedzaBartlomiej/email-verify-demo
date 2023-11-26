package pl.bartlomiej.emailverifydemo.log;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bartlomiej.emailverifydemo.exceptions.global.NoContentException;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    @Override
    public void createLog(String eventMessage) {
        Date eventTime = new Date(System.currentTimeMillis());
        Log newLog = new Log(eventTime, eventMessage);
        logRepository.save(newLog);
    }

    @Override
    public List<Log> getLogs() {
        List<Log> logs = logRepository.findAll();
        if (logs.isEmpty()) {
            throw new NoContentException();
        } else {
            return logs;
        }
    }
}
