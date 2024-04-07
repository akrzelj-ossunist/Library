package com.maurer.library.models;

import com.maurer.library.models.enums.Genre;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/**
 *
 * Info about book table
 **/
@Data
@Entity
@Table(name="book")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @Column
    private Boolean isAvilable;

    @Column
    private String note;

    @Column(name = "created_date")
    private Date createdDate = new Date();

    @Column
    private String isbn;

    @Column
    @Enumerated(EnumType.STRING)
    private Genre genre;
}
