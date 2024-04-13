package com.maurer.library.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/**
 *
 *  Information about book author
 *
 **/
@Data
@Entity
@Table(name="author")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "full_name")
    private String fullName;

    @Column
    private Date birthday;

}
