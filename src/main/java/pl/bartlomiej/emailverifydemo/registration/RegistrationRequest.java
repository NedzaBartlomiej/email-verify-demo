package pl.bartlomiej.emailverifydemo.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegistrationRequest(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String password
) {
}
