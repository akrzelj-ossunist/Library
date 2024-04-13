package com.maurer.library.models;

import com.maurer.library.models.enums.Status;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Entity
@Table(name="rent")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "lending_date")
    private Date lendingDate;

    @Column(name = "return_date")
    @Nullable
    private Date returnDate;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    @Nullable
    private String note;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;
}
