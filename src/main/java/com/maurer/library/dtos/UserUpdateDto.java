package com.maurer.library.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserUpdateDto {

    @NotBlank(message = "User name is mandatory")
    private String fullName;

    @NotBlank(message = "User address is mandatory")
    private String address;

    @NotBlank(message = "User birthday is mandatory")
    private Date birthday;
}
