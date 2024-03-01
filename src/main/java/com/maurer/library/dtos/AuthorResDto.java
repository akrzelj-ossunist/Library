package com.maurer.library.dtos;

import lombok.*;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AuthorResDto {

    private String id;

    private String fullName;

    private Date birthday;
}
