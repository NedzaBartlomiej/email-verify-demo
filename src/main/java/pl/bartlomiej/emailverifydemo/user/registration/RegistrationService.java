package pl.bartlomiej.emailverifydemo.user.registration;

import jakarta.servlet.http.HttpServletRequest;
import pl.bartlomiej.emailverifydemo.user.User;

import java.util.Map;

public interface RegistrationService {
    User registerUser(RegistrationRequest registrationRequest, final HttpServletRequest servletRequest);

    Map<String, Object> createSuccessfulRegistrationResponse(User registeredUser, String responseMessage);
}
