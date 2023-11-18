package pl.bartlomiej.emailverifydemo.registration;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.bartlomiej.emailverifydemo.user.User;

import java.util.Map;

public interface RegistrationService {

    enum RegistrationStatus {
        NULL_CREDENTIALS, USER_EXISTS, VALID
    }

    @Getter
    @AllArgsConstructor
    class RegistrationResponse {
        private RegistrationStatus registrationStatus;
        private User registeredUser;
    }

    RegistrationResponse registerUser(RegistrationRequest registrationRequest, final HttpServletRequest servletRequest);

    Map<String, Object> createSuccessfulRegistrationResponse(User registeredUser, String responseMessage);
}
