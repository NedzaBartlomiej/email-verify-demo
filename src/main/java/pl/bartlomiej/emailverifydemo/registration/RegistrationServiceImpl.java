package pl.bartlomiej.emailverifydemo.registration;

import org.springframework.stereotype.Service;
import pl.bartlomiej.emailverifydemo.user.User;

import java.util.HashMap;
import java.util.Map;

@Service
public class RegistrationServiceImpl implements RegistrationService{
    @Override
    public Map<String, Object> createRegistrationResponse(User registeredUser, String responseMessage) {
        Map<String, Object> response = new HashMap<>();
        response.put("registeredUser", registeredUser);
        response.put("responseMessage", responseMessage);
        return response;
    }
}
