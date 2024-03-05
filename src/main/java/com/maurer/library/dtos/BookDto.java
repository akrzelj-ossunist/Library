package com.maurer.library.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDto {

    @NotBlank(message = "Book title is mandatory")
    @Size(min=2, max=30)
    private String title;

    @NotBlank(message = "Author name is mandatory")
    @Size(min=4, max=30)
    private String author;

    @Nullable
    @Size(max=300)
    private String note;

    @NotBlank(message = "Book ISBN is mandatory")
    @Size(min=10, max=10)
    private String isbn;

    @NotBlank(message = "Book genre is mandatory")
    @Size(min=4, max=10)
    private String genre;
}
