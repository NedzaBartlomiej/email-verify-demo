package pl.bartlomiej.emailverifydemo.exceptions.user;

public class UserHasRoleException extends RuntimeException {
    public UserHasRoleException(String assignedRole) {
        super("User already have " + assignedRole + " role.");
    }
}
