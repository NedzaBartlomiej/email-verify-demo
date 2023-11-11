package pl.bartlomiej.emailverifydemo.registration.token;

public interface TokenService {
    enum TokenValidateStatus {
        NOT_EXISTS, EXPIRED, VALID
    }

    public VerifyToken findByToken(String token);

    TokenValidateStatus validateVerifyToken(String token);
}
