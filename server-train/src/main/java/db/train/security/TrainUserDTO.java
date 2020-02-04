package db.train.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TrainUserDTO {

    @NotBlank
    @Size(min=4, max=16)
    private String username;
    @NotBlank
    @Size(min=8, max=16)
    private String password;
    @NotBlank
    private String confirmPassword;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Email
    private String confirmEmail;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;

    @AssertTrue(message="You didn't confirm password")
    public boolean validPassword() {
        return password.equals(confirmPassword);
    }

    @AssertTrue(message="You didn't confirm email")
    public boolean validEmail() {
        return email.equals(confirmEmail);
    }

}
