package pl.bartlomiej.emailverifydemo.registration;

import pl.bartlomiej.emailverifydemo.user.User;

import java.util.Map;

public interface RegistrationService {
    Map<String, Object> createRegistrationResponse(User registeredUser, String responseMessage);
}
