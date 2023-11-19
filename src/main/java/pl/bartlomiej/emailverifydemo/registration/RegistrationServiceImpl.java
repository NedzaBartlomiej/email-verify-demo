package pl.bartlomiej.emailverifydemo.registration;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.bartlomiej.emailverifydemo.event.RegistrationCompleteEvent;
import pl.bartlomiej.emailverifydemo.log.LogService;
import pl.bartlomiej.emailverifydemo.user.User;
import pl.bartlomiej.emailverifydemo.user.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final LogService logService;

    @Override
    public RegistrationResponse registerUser(RegistrationRequest registrationRequest, HttpServletRequest servletRequest) {

        boolean isRegistrationRequestFieldsAreBlankOrNull =
                Stream.of(RegistrationRequest.class.getDeclaredFields()).map(field -> {
                    field.setAccessible(true);
                    try {
                        return field.get(registrationRequest).toString();
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }

                }).anyMatch(value -> value == null || value.isBlank());

        if (isRegistrationRequestFieldsAreBlankOrNull) {
            return new RegistrationResponse(RegistrationStatus.BAD_CREDENTIALS, null);
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
