package pl.bartlomiej.emailverifydemo.event.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.bartlomiej.emailverifydemo.event.RegistrationCompleteEvent;
import pl.bartlomiej.emailverifydemo.user.User;
import pl.bartlomiej.emailverifydemo.user.UserServiceImpl;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private final UserServiceImpl userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // 1. get the new registered user
        User validRegisteredUser = event.getUser();
        // 2. create a verification token for registered user
        String verifyToken = UUID.randomUUID().toString();
        // 3. save the verification token
        userService.saveUserVerifyToken(validRegisteredUser, verifyToken);
        // 4. build the verification url to be sent to user email
        String verifyEmailUrl = event.getAppUrl() + "/verify?token=" + verifyToken;
        // 5. send the email
        log.info("Click to verify your account: " + verifyEmailUrl);
    }
}
