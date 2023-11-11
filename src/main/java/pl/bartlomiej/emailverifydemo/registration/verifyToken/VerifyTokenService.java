package pl.bartlomiej.emailverifydemo.registration.verifyToken;

import java.util.List;

public interface VerifyTokenService {
    enum TokenValidateStatus {
        NOT_EXISTS, EXPIRED, VALID
    }

    public VerifyToken findByToken(String token);

    TokenValidateStatus validateVerifyToken(String token);
}
