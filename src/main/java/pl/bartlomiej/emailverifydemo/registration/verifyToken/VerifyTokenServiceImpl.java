package pl.bartlomiej.emailverifydemo.registration.verifyToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bartlomiej.emailverifydemo.user.User;
import pl.bartlomiej.emailverifydemo.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class VerifyTokenServiceImpl implements VerifyTokenService {

    private final VerifyTokenRepository verifyTokenRepository;
    private final UserService userService;
    private final VerifyTokenServiceImpl self;

    @Override
    @Cacheable(cacheNames = "tokenCache", cacheManager = "validateVerifyTokenCacheManager")
    public VerifyToken findByToken(String token) {
        return verifyTokenRepository.findByToken(token);
    }

    @Override
    public TokenValidateStatus validateVerifyToken(String token) {
        VerifyToken verifyToken = self.findByToken(token);
        if (token == null || verifyToken == null) {
            return TokenValidateStatus.NOT_EXISTS;
        } else if ((verifyToken.getExpirationTime().getTime() - System.currentTimeMillis()) <= 0) {
            return TokenValidateStatus.EXPIRED;
        } else {
            if(verifyToken.getUser().isEnabled()) {
                return TokenValidateStatus.USED;
            }
            User user = verifyToken.getUser();
            user.setEnabled(true);
            userService.save(user);
            return TokenValidateStatus.VALID;
        }
    }

    //todo: add to verifytoken 
    @Scheduled(cron = "0 0/10 * * * ?")
    @Transactional
    public void deleteExpiredToken() {
        List<VerifyToken> expiredVerifyTokens = verifyTokenRepository.findExpiredVerifyTokens();
        if (!expiredVerifyTokens.isEmpty()) {
            verifyTokenRepository.deleteAll(expiredVerifyTokens);
            log.info("{} expired tokens have been removed.", expiredVerifyTokens.size());
        } else {
            log.info("No tokens found to remove.");
            //todo: make a table in db for logs and give permits to show to ADMIN
        }
    }
}
