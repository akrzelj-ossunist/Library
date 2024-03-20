package com.maurer.library.repositories;

import com.maurer.library.models.Author;
import com.maurer.library.models.Book;
import com.maurer.library.models.enums.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    Optional<Book> findByTitle(String title);

    List<Book> findByIsAvilable(Boolean isAvailable);

    List<Book> findByGenre(String genre);

    List<Book> findByAuthor(Author author);

    Optional<Book> findByIsbn(String value);

    @Query("SELECT b FROM Book b " +
            "WHERE (:title IS NULL OR b.title = :title) " +
            "AND (:author IS NULL OR b.author = :author) " +
            "AND (:isAvailable IS NULL OR b.isAvilable = :isAvailable) " +
            "AND (:isbn IS NULL OR b.isbn = :isbn) " +
            "AND (:genre IS NULL OR b.genre = :genre)")
    Page<Book> findByTitleAndAuthorAndIsAvailableAndIsbnAndGenre(
            String title, Author author, Boolean isAvailable, String isbn, Genre genre, Pageable pageable);
}
