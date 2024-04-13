package com.maurer.library.repositories;

import com.maurer.library.models.Book;
import com.maurer.library.models.RentEntry;
import com.maurer.library.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query("SELECT r FROM RentEntry r " +
            "WHERE (:user IS NULL OR r.user = :user) " +
            "AND (:book IS NULL OR r.book = :book) " +
            "AND (:status IS NULL OR r.status = :status) " +
            "AND (:lendingDate IS NULL OR r.lendingDate = :lendingDate) " +
            "AND (:returnDate IS NULL OR r.returnDate = :returnDate)")
    Page<RentEntry> findByUserAndBookAndStatusAndLendingDateAndReturnDate(User user, Book book, String status, Date lendingDate, Date returnDate, Pageable pageable);
}
