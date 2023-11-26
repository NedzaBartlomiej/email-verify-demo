package pl.bartlomiej.emailverifydemo.user.registration.verify_token;

public interface VerifyTokenService {

    public VerifyToken findByToken(String token);

    void validateVerifyToken(String token);
}
