package com.maurer.library.dtos;

import com.maurer.library.models.enums.UserRole;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class UserResDto {

    private String id;

    private String fullName;

    private String email;

    private String address;

    private Date birthday;

    private UserRole role;
}
