package pl.bartlomiej.emailverifydemo.exceptions.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not found.");
    }
}
