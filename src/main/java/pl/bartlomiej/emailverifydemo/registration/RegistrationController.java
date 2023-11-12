package pl.bartlomiej.emailverifydemo.registration;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bartlomiej.emailverifydemo.event.RegistrationCompleteEvent;
import pl.bartlomiej.emailverifydemo.registration.verifyToken.VerifyTokenService;
import pl.bartlomiej.emailverifydemo.registration.verifyToken.VerifyToken;
import pl.bartlomiej.emailverifydemo.user.User;
import pl.bartlomiej.emailverifydemo.user.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/register")
public class RegistrationController {
    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerifyTokenService tokenService;

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest servletRequest) {
        if(userService.findByEmail(registrationRequest.email()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Provided email already exists.");
        } else {
            User registeredUser = userService.registerUser(registrationRequest);
            publisher.publishEvent(new RegistrationCompleteEvent(registeredUser, getAppUrl(servletRequest)));
            var acceptedRegisteredUserResponse = createSuccesfulRegistrationResponse(registeredUser, "Registration complete. An activation link has been sent to your email.");
            return ResponseEntity.accepted().body(acceptedRegisteredUserResponse);
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String verifyToken) {
        VerifyTokenService.TokenValidateStatus tokenValidateStatus = tokenService.validateVerifyToken(verifyToken);
        switch (tokenValidateStatus) {
            case NOT_EXISTS -> {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token does not exist.");
            }
            case EXPIRED -> {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Token is expired.");
            }
            case USED -> {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Your account is already verified.");
            }
            case VALID -> {
                return ResponseEntity.status(HttpStatus.OK).body("Your account has been successfully verified, you can log in.");
            }
            default -> {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unhandled case. Something went wrong.");
            }
        }
    }

    private String getAppUrl(HttpServletRequest servletRequest) {
        return String.valueOf(servletRequest.getRequestURL());
    }


    private Map<String, Object> createSuccesfulRegistrationResponse(User registeredUser, String responseMessage) {
        Map<String, Object> response = new HashMap<>();
        response.put("registeredUser", registeredUser);
        response.put("responseMessage", responseMessage);
        return response;
    }
}
