package pl.bartlomiej.emailverifydemo.registration;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bartlomiej.emailverifydemo.registration.verifyToken.VerifyTokenService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final VerifyTokenService tokenService;

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest servletRequest) {
        RegistrationService.RegistrationResponse registrationResponse = registrationService.registerUser(registrationRequest, servletRequest);
        switch (registrationResponse.getRegistrationStatus()) {
            case BAD_CREDENTIALS -> {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            case USER_EXISTS -> {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User with email: " + registrationRequest.email() + " already exist.");
            }
            case VALID -> {
                Map<String, Object> successfulRegistrationResponse = registrationService.createSuccessfulRegistrationResponse(registrationResponse.getRegisteredUser(), "Registration complete. An activation link has been sent to your email.");
                return ResponseEntity.status(HttpStatus.CREATED).body(successfulRegistrationResponse);
            }
            default -> {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unhandled case. Something went wrong.");
            }
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String verifyToken) {
        VerifyTokenService.TokenValidateStatus tokenValidateStatus = tokenService.validateVerifyToken(verifyToken);
        switch (tokenValidateStatus) {
            case NOT_EXISTS -> {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token does not exist.");
            }
            case EXPIRED -> {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Token is expired.");
            }
            case USED -> {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Account is already verified.");
            }
            case VALID -> {
                return ResponseEntity.status(HttpStatus.OK).body("Account has been successfully verified, you can log in.");
            }
            default -> {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unhandled case. Something went wrong.");
            }
        }
    }
}
