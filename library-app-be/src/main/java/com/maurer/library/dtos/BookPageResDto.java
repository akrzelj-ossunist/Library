package com.maurer.library.dtos;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class BookPageResDto {

    public int page;

    public int size;

    public List<BookResDto> bookList;

    public int pageTotal;
}
