package pl.bartlomiej.emailverifydemo.registration.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bartlomiej.emailverifydemo.user.User;
import pl.bartlomiej.emailverifydemo.user.UserService;

import java.util.Calendar;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

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

    private void deleteExpiredToken() {

    }
}
