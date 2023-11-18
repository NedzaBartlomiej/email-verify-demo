package pl.bartlomiej.emailverifydemo.registration;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.bartlomiej.emailverifydemo.event.RegistrationCompleteEvent;
import pl.bartlomiej.emailverifydemo.log.LogService;
import pl.bartlomiej.emailverifydemo.user.User;
import pl.bartlomiej.emailverifydemo.user.UserService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final LogService logService;

    @Override
    public RegistrationResponse registerUser(RegistrationRequest registrationRequest, HttpServletRequest servletRequest) {
        if (Arrays.stream(registrationRequest.getClass().getDeclaredFields()).anyMatch(Objects::isNull)) {
            return new RegistrationResponse(RegistrationStatus.NULL_CREDENTIALS, null);
        } else if (userService.findByEmail(registrationRequest.email()).isPresent()) {
            return new RegistrationResponse(RegistrationStatus.USER_EXISTS, null);
        } else {
            User registeredUser = userService.registerUser(registrationRequest);
            publisher.publishEvent(new RegistrationCompleteEvent(registeredUser, getRequestedAppUrl(servletRequest)));
            logService.createLog("User with email: " + registeredUser.getEmail() + " has been registered.");
            return new RegistrationResponse(RegistrationStatus.VALID, registeredUser);
        }
    }

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
