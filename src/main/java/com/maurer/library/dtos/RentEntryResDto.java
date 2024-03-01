package com.maurer.library.dtos;

import com.maurer.library.models.Book;
import com.maurer.library.models.User;
import com.maurer.library.models.enums.Status;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class RentEntryResDto {

    private String id;

    private Date lendingDate;

    private Date returnDate;

    private Status status;

    private String note;

    private Book book;

    private User user;
}
