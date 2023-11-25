package pl.bartlomiej.emailverifydemo.registration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bartlomiej.emailverifydemo.exceptions.user.UserExistsException;
import pl.bartlomiej.emailverifydemo.exceptions.registration.VerifyTokenConflictException;
import pl.bartlomiej.emailverifydemo.registration.verify_token.VerifyTokenService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register") //todo: fix endpoints naming and mappings
public class RegistrationController {

    private final RegistrationService registrationService;
    private final VerifyTokenService tokenService;

    //todo: HATEOAS -> link to /users when created user
    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest, final HttpServletRequest servletRequest) {
        RegistrationService.RegistrationResponse registrationResponse = registrationService.registerUser(registrationRequest, servletRequest);
        switch (registrationResponse.getRegistrationStatus()) {
            case USER_EXISTS -> {
                throw new UserExistsException();
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

    @PatchMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String verifyToken) {
        VerifyTokenService.TokenValidateStatus tokenValidateStatus = tokenService.validateVerifyToken(verifyToken);
        switch (tokenValidateStatus) {
            case NOT_EXISTS -> {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            case EXPIRED, USED -> {
                throw new VerifyTokenConflictException(tokenValidateStatus);
            }
            case VALID -> {
                return ResponseEntity.status(HttpStatus.OK).body("Account has been successfully verified, you can log in.");
            }
            default -> {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unhandled case. Something went wrong.");
            }
        }
    }

//    @GetMapping("verify/resendVerifyToken")
//    public ResponseEntity<String> resendVerifyToken(@PathVariable Long userId) {
//        //todo: resend verify token after validate:
//        // - isUserEnabled -> USER_IS_ENABLED
//        // - isUserTokenExpired -> TOKEN_IS_CURRENT
//        // - VALID -> publish registrationCompleteEvent (edit name for: CanSendVerifyTokenEvent)
//    }
}
