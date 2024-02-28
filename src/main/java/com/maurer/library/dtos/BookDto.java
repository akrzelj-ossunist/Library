package com.maurer.library.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BookDto {

    @NotBlank(message = "Book title is mandatory")
    private String title;

    @NotBlank(message = "Author name is mandatory")
    private String author;

    private String note;

    @NotBlank(message = "Book ISBN is mandatory")
    private String isbn;

    @NotBlank(message = "Book genre is mandatory")
    private String genre;
}
