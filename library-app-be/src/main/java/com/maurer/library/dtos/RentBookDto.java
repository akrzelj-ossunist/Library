package com.maurer.library.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RentBookDto {

    private String userId;

    private String bookId;
}
