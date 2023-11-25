package pl.bartlomiej.emailverifydemo.exceptions.registration;

import pl.bartlomiej.emailverifydemo.registration.verify_token.VerifyTokenService;

public class VerifyTokenConflictException extends RuntimeException {
    public VerifyTokenConflictException(VerifyTokenService.TokenValidateStatus tokenValidateStatus) {
        super(
                tokenValidateStatus.equals(VerifyTokenService.TokenValidateStatus.EXPIRED)
                ? "Token is expired." : "Account is already verified.");
    }
}
