package pl.bartlomiej.emailverifydemo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        List<User> userList = userService.getUsers();
        if(!userList.isEmpty()) {
            return ResponseEntity.ok().body(userList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
