package pl.bartlomiej.emailverifydemo.user.registration;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.bartlomiej.emailverifydemo.event.RegistrationCompleteEvent;
import pl.bartlomiej.emailverifydemo.exceptions.user.UserExistsException;
import pl.bartlomiej.emailverifydemo.log.LogService;
import pl.bartlomiej.emailverifydemo.user.User;
import pl.bartlomiej.emailverifydemo.user.UserService;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final LogService logService;

    @Override
    public User registerUser(RegistrationRequest registrationRequest, HttpServletRequest servletRequest) {

        if (userService.findByEmail(registrationRequest.email()).isPresent()) {
            throw new UserExistsException();
        } else {
            User registeredUser = userService.registerUser(registrationRequest);
            publisher.publishEvent(new RegistrationCompleteEvent(registeredUser, getRequestedAppUrl(servletRequest)));
            logService.createLog("User with email: " + registeredUser.getEmail() + " has been registered.");
            return registeredUser;
        }
    }

    @Override
    public Map<String, Object> createSuccessfulRegistrationResponse(User registeredUser, String responseMessage) {
        Map<String, Object> response = new HashMap<>();
        response.put("registeredUser", registeredUser);
        response.put("responseMessage", responseMessage);
        return response;
    }

    private String getRequestedAppUrl(HttpServletRequest servletRequest) {
        return String.valueOf(servletRequest.getRequestURL());
    }
}
