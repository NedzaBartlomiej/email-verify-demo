package pl.bartlomiej.emailverifydemo.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.bartlomiej.emailverifydemo.exceptions.global.GlobalRestControllerException;
import pl.bartlomiej.emailverifydemo.exceptions.global.NoContentException;
import pl.bartlomiej.emailverifydemo.exceptions.global.ResourceNotFoundException;
import pl.bartlomiej.emailverifydemo.exceptions.registration.UsedTokenException;
import pl.bartlomiej.emailverifydemo.exceptions.user.UserExistsException;
import pl.bartlomiej.emailverifydemo.exceptions.user.UserHasRoleException;
import pl.bartlomiej.emailverifydemo.user.registration.RegistrationRequest;
import pl.bartlomiej.emailverifydemo.user.registration.RegistrationService;
import pl.bartlomiej.emailverifydemo.user.registration.verify_token.VerifyTokenService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final RegistrationService registrationService;
    private final VerifyTokenService tokenService;

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        try {
            List<User> users = userService.getUsers();
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } catch (NoContentException exception) {
            throw new GlobalRestControllerException(exception, exception.getHttpStatus());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.status(HttpStatus.OK).body("User has been deleted.");
        } catch (ResourceNotFoundException exception) {
            throw new GlobalRestControllerException(exception, exception.getHttpStatus());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{userId}/roles")
    public ResponseEntity<?> grantAdminRole(@PathVariable Long userId) {
        try {
            userService.grantAdminRole(userId);
            return ResponseEntity.status(HttpStatus.OK).body("Granted administrator privileges.");
        } catch (UserHasRoleException | ResourceNotFoundException exception) {
            if (exception instanceof UserHasRoleException) {
                throw new GlobalRestControllerException(exception, exception.getHttpStatus());
            } else {
                throw new GlobalRestControllerException(exception, exception.getHttpStatus());
            }
        }
    }

    @PostMapping
    public ResponseEntity<User> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest, final HttpServletRequest servletRequest) {
        try {
            User registeredUser = registrationService.registerUser(registrationRequest, servletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (UserExistsException exception) {
            throw new GlobalRestControllerException(exception, exception.getHttpStatus());
        }
    }

    @PatchMapping
    public ResponseEntity<?> verifyUser(@RequestParam("token") String verifyToken) {
        try {
            tokenService.validateVerifyToken(verifyToken);
            return ResponseEntity.status(HttpStatus.OK).body("Your account has been verified, you can log in.");
        } catch (ResourceNotFoundException | UsedTokenException exception) {
            throw new GlobalRestControllerException(exception, exception.getHttpStatus());
        }
    }

//    @GetMapping("/{userId}/verify-token")
//    public ResponseEntity<String> resendVerifyToken(@PathVariable Long userId) {
//        //todo: resend verify token after validate:
//        // - isUserEnabled -> USER_IS_ENABLED
//        // - isUserTokenExpired -> TOKEN_IS_CURRENT
//        // - VALID -> publish registrationCompleteEvent (edit name for: CanSendVerifyTokenEvent)
//    }
}
