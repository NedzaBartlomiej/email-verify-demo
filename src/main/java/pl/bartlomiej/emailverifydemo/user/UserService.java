package pl.bartlomiej.emailverifydemo.user;

import pl.bartlomiej.emailverifydemo.registration.RegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getUsers();
    User registerUser(RegistrationRequest request);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    void saveUserVerifyToken(User validRegisteredUser, String verifyToken);
    void saveUser(User user);
    void deleteUser(Long id);
    void grantAdminRole(Long id);
}
