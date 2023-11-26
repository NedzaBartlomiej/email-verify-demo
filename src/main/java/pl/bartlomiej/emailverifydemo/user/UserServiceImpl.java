package pl.bartlomiej.emailverifydemo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bartlomiej.emailverifydemo.exceptions.user.UserHasRoleException;
import pl.bartlomiej.emailverifydemo.exceptions.global.ResourceNotFoundException;
import pl.bartlomiej.emailverifydemo.user.registration.RegistrationRequest;
import pl.bartlomiej.emailverifydemo.user.registration.verify_token.VerifyToken;
import pl.bartlomiej.emailverifydemo.user.registration.verify_token.VerifyTokenRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final VerifyTokenRepository verifyTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Cacheable(cacheNames = "userCache", cacheManager = "usersCacheManager")
    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User registerUser(RegistrationRequest requestUser) {
        User registeredUser = new User(
                requestUser.firstName(),
                requestUser.lastName(),
                requestUser.email(),
                passwordEncoder.encode(requestUser.password()));
        System.out.println(requestUser.email());
        return userRepository.save(registeredUser);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUserVerifyToken(User validRegisteredUser, String verifyToken) {
        VerifyToken token = new VerifyToken(verifyToken, validRegisteredUser);
        verifyTokenRepository.save(token);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @Override
    @Transactional
    public void grantAdminRole(Long id) {
        Optional<User> user = userRepository.findById(id);
        final String assignedRole = "ADMIN";
        if (user.isPresent()) {
            if (user.get().getRole().equals("USER")) {
                userRepository.updateRoleById(assignedRole, id);
            } else {
                throw new UserHasRoleException(assignedRole);
            }
        } else {
            throw new ResourceNotFoundException();
        }
    }
}
