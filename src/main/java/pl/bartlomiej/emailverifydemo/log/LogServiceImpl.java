package pl.bartlomiej.emailverifydemo.log;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

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
}
