package pl.bartlomiej.emailverifydemo.user.registration.verify_token;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bartlomiej.emailverifydemo.exceptions.global.ResourceNotFoundException;
import pl.bartlomiej.emailverifydemo.exceptions.registration.ExpiredTokenException;
import pl.bartlomiej.emailverifydemo.exceptions.registration.UsedTokenException;
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
    public void validateVerifyToken(String token) {
        VerifyToken verifyToken = self.findByToken(token);
        if (token == null || verifyToken == null) {
            throw new ResourceNotFoundException();
        } else if ((verifyToken.getExpirationTime().getTime() - System.currentTimeMillis()) <= 0) {
            throw new ExpiredTokenException(verifyToken.getExpirationTime());
        } else if (verifyToken.getUser().isEnabled()) {
            throw new UsedTokenException();
        } else {
            User user = verifyToken.getUser();
            user.setEnabled(true);
            userService.saveUser(user);
            verifyToken.setUsed(true);
            verifyTokenRepository.save(verifyToken);
            logService.createLog("User with email: " + user.getEmail() + " has been verified");
        }
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    @Transactional
    public void deleteExpiredOrUsedTokens() {
        List<VerifyToken> expiredVerifyTokens = verifyTokenRepository.findExpiredVerifyTokens();
        List<VerifyToken> usedVerifyTokens = verifyTokenRepository.findUsedVerifyTokens();
        if (!expiredVerifyTokens.isEmpty() || !usedVerifyTokens.isEmpty()) {
            verifyTokenRepository.deleteAll(expiredVerifyTokens);
            verifyTokenRepository.deleteAll(usedVerifyTokens);
            logService.createLog(expiredVerifyTokens.size() + " expired tokens have been removed.");
            logService.createLog(usedVerifyTokens.size() + " used tokens have been removed.");
        } else {
            logService.createLog("No tokens found to remove.");
        }
    }
}
