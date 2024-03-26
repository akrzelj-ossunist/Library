package com.maurer.library.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class AuthorDto {

    @NotBlank(message = "Author name is mandatory")
    @Size(min=4, max=30)
    private String fullName;

    @NotNull(message = "Author birthday is mandatory")
    private Date birthday;
}
