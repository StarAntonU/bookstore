package mate.academy.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
//@FieldMatch(first = "password", second = "repeatPassword")
public class UserRegistrationRequestDto {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 4, max = 20)
    private String password;
    @NotBlank
    @Size(min = 4, max = 20)
    private String repeatPassword;
    @NotBlank
    @Size(min = 2, max = 64)
    private String firstName;
    @NotBlank
    @Size(min = 2, max = 64)
    private String lastName;
    @Size(min = 3, max = 100)
    private String shippingAddress;
}
