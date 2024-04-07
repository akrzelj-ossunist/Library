package com.maurer.library.dtos;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class UserDto {

    @NotBlank(message = "User name is mandatory")
    @Size(min=4, max=30)
    private String fullName;

    @NotBlank(message = "User email is mandatory")
    @Email
    private String email;

    @NotBlank(message = "Email repeat cannot be empty")
    @Email
    private String emailRepeat;

    @NotBlank(message = "User password is mandatory")
    //@Pattern(regexp = "\"(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}\"")
    private String password;

    @NotBlank(message = "Password repeat cannot be empty")
    //@Pattern(regexp = "\"(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}\"")
    private String passwordRepeat;

    @NotBlank(message = "User address is mandatory")
    @Size(min=4, max=30)
    private String address;

    @NotNull(message = "User birthday is mandatory")
    private Date birthday;
}
