package pl.bartlomiej.emailverifydemo.registration.verify_token;

public interface VerifyTokenService {
    enum TokenValidateStatus {
        NOT_EXISTS, EXPIRED, USED, VALID
    }

    public VerifyToken findByToken(String token);

    TokenValidateStatus validateVerifyToken(String token);
}
