package pl.bartlomiej.emailverifydemo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bartlomiej.emailverifydemo.exceptions.GlobalRestControllerException;
import pl.bartlomiej.emailverifydemo.exceptions.user.UserHasRoleException;
import pl.bartlomiej.emailverifydemo.exceptions.user.UserNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getUsers();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(users);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        if (userService.findById(userId).isPresent()) {
            userService.deleteUser(userId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping("/{userId}/roles")
    public ResponseEntity<?> grantAdminRole(@PathVariable Long userId) {
        try {
            userService.grantAdminRole(userId);
        } catch (UserHasRoleException | UserNotFoundException exception) {
            if (exception instanceof UserHasRoleException) {
                throw new GlobalRestControllerException(exception, HttpStatus.CONFLICT);
            } else {
                throw new GlobalRestControllerException(exception, HttpStatus.NOT_FOUND);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("Granted administrator privileges.");
    }
}
