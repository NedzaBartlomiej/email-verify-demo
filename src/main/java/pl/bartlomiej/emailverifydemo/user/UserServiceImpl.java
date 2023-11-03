package pl.bartlomiej.emailverifydemo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.bartlomiej.emailverifydemo.exception.UserAlreadyExistException;
import pl.bartlomiej.emailverifydemo.registration.RegistrationRequest;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User registerUser(RegistrationRequest requestUser) {
        Optional<User> optionalUser = userRepository.findByEmail(requestUser.email());
        if(optionalUser.isPresent()) {
            throw new UserAlreadyExistException(requestUser.email());
        }
        User registeredUser = new User(
                requestUser.firstName(),
                requestUser.lastName(),
                requestUser.email(),
                passwordEncoder.encode(requestUser.password()),
                requestUser.role());
        return userRepository.save(registeredUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
