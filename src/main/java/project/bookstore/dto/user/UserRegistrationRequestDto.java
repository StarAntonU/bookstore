package project.bookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import project.bookstore.annotation.FieldMatch;

@Data
@FieldMatch(firstPassName = "password", secondPassName = "repeatedPassword")
public class UserRegistrationRequestDto {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 4, max = 20)
    private String password;
    @NotBlank
    @Size(min = 4, max = 20)
    private String repeatedPassword;
    @NotBlank
    @Size(min = 2, max = 64)
    private String firstName;
    @NotBlank
    @Size(min = 2, max = 64)
    private String lastName;
    @Size(min = 3, max = 100)
    private String shippingAddress;
}
