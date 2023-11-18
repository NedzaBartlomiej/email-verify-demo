package pl.bartlomiej.emailverifydemo.registration.verifyToken;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bartlomiej.emailverifydemo.log.LogService;
import pl.bartlomiej.emailverifydemo.user.User;
import pl.bartlomiej.emailverifydemo.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class VerifyTokenServiceImpl implements VerifyTokenService {

    private final VerifyTokenRepository verifyTokenRepository;
    private final UserService userService;
    private final VerifyTokenServiceImpl self;
    private final LogService logService;

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
            if (verifyToken.getUser().isEnabled()) {
                return TokenValidateStatus.USED;
            }
            User user = verifyToken.getUser();
            user.setEnabled(true);
            userService.save(user);
            verifyToken.setUsed(true);
            verifyTokenRepository.save(verifyToken);
            logService.createLog("User with email: " + user.getEmail() + " has been verified");
            return TokenValidateStatus.VALID;
        }
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    @Transactional
    public void deleteExpiredOrUsedTokens() {
        List<VerifyToken> expiredVerifyTokens = verifyTokenRepository.findExpiredVerifyTokens();
        List<VerifyToken> usedVerifyTokens = verifyTokenRepository.findUsedVerifyTokens();
        if (!expiredVerifyTokens.isEmpty() || !usedVerifyTokens.isEmpty()) {
            verifyTokenRepository.deleteAll(expiredVerifyTokens);
            logService.createLog(expiredVerifyTokens.size() + " expired tokens have been removed.");
            logService.createLog(usedVerifyTokens.size() + " used tokens have been removed.");
        } else {
            logService.createLog("No tokens found to remove.");
        }
    }
}
