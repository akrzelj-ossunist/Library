package com.maurer.library.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookUpdateDto {

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
