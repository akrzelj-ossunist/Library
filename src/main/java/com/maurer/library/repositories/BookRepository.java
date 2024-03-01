package com.maurer.library.repositories;

import com.maurer.library.models.Author;
import com.maurer.library.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    Optional<Book> findByTitle(String title);

    List<Book> findByIsAvilable(Boolean isAvailable);

    List<Book> findByGenre(String genre);

    List<Book> findByAuthor(Author author);

    Optional<Book> findByIsbn(String value);
}
