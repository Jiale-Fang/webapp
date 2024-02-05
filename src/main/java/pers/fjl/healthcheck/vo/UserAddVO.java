package pers.fjl.healthcheck.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserAddVO {
    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @Email(message = "Username must be an email addr")
    @NotBlank(message = "Username cannot be blank")
    private String username;
}
