package com.maurer.library.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordDto {

    @NotBlank(message = "Password field cannot be null")
    private String password;

    @NotBlank(message = "Repeat password field cannot be null")
    private String passwordRepeat;
}
