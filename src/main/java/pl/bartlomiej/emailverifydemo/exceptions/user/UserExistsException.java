package pl.bartlomiej.emailverifydemo.exceptions.user;

public class UserExistsException extends RuntimeException {
    public UserExistsException() {
        super("User with provided email is already exist.");
    }
}
