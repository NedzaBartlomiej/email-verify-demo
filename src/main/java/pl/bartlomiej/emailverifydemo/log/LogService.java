package pl.bartlomiej.emailverifydemo.log;

import java.util.List;

public interface LogService {
    void createLog(String eventMessage);
    List<Log> getLogs();
}
