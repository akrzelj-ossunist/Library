package com.maurer.library.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class UserDto {

    @NotBlank(message = "User name is mandatory")
    private String fullName;

    @NotBlank(message = "User email is mandatory")
    private String email;

    @NotBlank(message = "Email repeat cannot be empty")
    private String emailRepeat;

    @NotBlank(message = "User password is mandatory")
    private String password;

    @NotBlank(message = "Password repeat cannot be empty")
    private String passwordRepeat;

    @NotBlank(message = "User address is mandatory")
    private String address;

    @NotNull(message = "User birthday is mandatory")
    private Date birthday;
}
