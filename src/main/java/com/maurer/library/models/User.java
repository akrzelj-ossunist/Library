package com.maurer.library.models;

import com.maurer.library.models.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/**
*
*   Keeps information about users
*
**/
@Data
@Entity
@Table(name="users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "full_name")
    private String fullName;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String address;

    @Column
    private Date birthday;

    @Column
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
