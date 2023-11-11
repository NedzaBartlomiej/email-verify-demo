package pl.bartlomiej.emailverifydemo.registration.verifyToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bartlomiej.emailverifydemo.user.User;
import pl.bartlomiej.emailverifydemo.user.UserService;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerifyTokenServiceImpl implements VerifyTokenService {

    private final VerifyTokenRepository verifyTokenRepository;
    private final UserService userService;
    @Override
    public VerifyToken findByToken(String token) {
        return verifyTokenRepository.findByToken(token);
        //todo: can do validation with enum used below
    }

    @Override
    public TokenValidateStatus validateVerifyToken(String token) {
        VerifyToken verifyToken = verifyTokenRepository.findByToken(token);
        User user = verifyToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if(token == null) {
            return TokenValidateStatus.NOT_EXISTS;
        }
        user.setEnabled(true);
        userService.save(user);
        return TokenValidateStatus.VALID;
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteExpiredToken() {
        List<VerifyToken> expiredVerifyTokens = verifyTokenRepository.findExpiredVerifyTokens();
        if(!expiredVerifyTokens.isEmpty()) {
            verifyTokenRepository.deleteAll(expiredVerifyTokens);
            log.info("{} expired tokens have been removed.", expiredVerifyTokens.size());
        } else {
            log.info("No tokens found to remove.");
            //todo: make a table in db for logs
        }
    }
}
