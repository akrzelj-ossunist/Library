package com.maurer.library.repositories;

import com.maurer.library.models.Book;
import com.maurer.library.models.RentEntry;
import com.maurer.library.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
public interface RentRepository extends JpaRepository<RentEntry, String> {
    List<RentEntry> findByUser(User userById);

    List<RentEntry> findByBook(Book bookById);

    List<RentEntry> findByStatus(String status);

    List<RentEntry> findByLendingDate(Date valid);

    List<RentEntry> findByReturnDate(Date valid);
}
