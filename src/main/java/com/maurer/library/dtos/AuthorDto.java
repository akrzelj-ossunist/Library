package com.maurer.library.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AuthorDto {

    @NotBlank(message = "Author name is mandatory")
    private String fullName;

    @NotBlank(message = "Author date of birth is mandatory")
    private Date birthday;
}
