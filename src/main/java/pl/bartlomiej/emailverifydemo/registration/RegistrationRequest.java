package pl.bartlomiej.emailverifydemo.registration;

public record RegistrationRequest(
        String firstName,
        String lastName,
        String email,
        String password
) { }
