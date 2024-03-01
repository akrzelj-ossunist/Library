package com.maurer.library.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AuthorDto {

    @NotBlank(message = "Author name is mandatory")
    private String fullName;

    @NotNull(message = "Author birthday is mandatory")
    private Date birthday;
}
