package pl.bartlomiej.emailverifydemo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//import pl.bartlomiej.emailverifydemo.exception.UserAlreadyExistException;
import pl.bartlomiej.emailverifydemo.registration.RegistrationRequest;
import pl.bartlomiej.emailverifydemo.registration.verifyToken.VerifyToken;
import pl.bartlomiej.emailverifydemo.registration.verifyToken.VerifyTokenRepository;

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
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUserVerifyToken(User validRegisteredUser, String verifyToken) {
        VerifyToken token = new VerifyToken(verifyToken, validRegisteredUser);
        verifyTokenRepository.save(token);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
