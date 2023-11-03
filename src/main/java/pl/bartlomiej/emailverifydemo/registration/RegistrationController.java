package pl.bartlomiej.emailverifydemo.registration;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bartlomiej.emailverifydemo.user.User;
import pl.bartlomiej.emailverifydemo.user.UserServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {
    private final UserServiceImpl userService;
    private final RegistrationServiceImpl registrationService;

    @PostMapping
    public ResponseEntity<?> registerUser(RegistrationRequest registrationRequest) {
        User registeredUser = userService.registerUser(registrationRequest);
        // publish registration event
        if(registrationRequest != null) {
            var acceptedRegisteredUserResponse = registrationService.createRegistrationResponse(registeredUser, "Registration successful. An activation link has been sent to your email.");
            return ResponseEntity.accepted().body(acceptedRegisteredUserResponse);
        } else {
            return ResponseEntity.badRequest().body("Registration failed. Check the data provided in the request.");
        }
    }
}
