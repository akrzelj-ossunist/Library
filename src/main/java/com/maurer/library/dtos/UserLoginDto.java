package com.maurer.library.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDto {

    @NotBlank(message = "User email is mandatory")
    private String email;

    @NotBlank(message = "User password is mandatory")
    private String password;
}
