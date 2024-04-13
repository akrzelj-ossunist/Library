package com.maurer.library.dtos;

import com.maurer.library.models.Author;
import com.maurer.library.models.enums.Genre;
import lombok.*;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BookResDto {

    private String id;

    private String title;

    private Author author;

    private Boolean isAvilable;

    private String note;

    private Date createdDate;

    private String isbn;

    private Genre genre;
}
