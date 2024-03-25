package com.maurer.library.repositories;

import com.maurer.library.models.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, String> {

    Optional<Author> findByFullName(String fullName);

    Page<Author> findByFullName(String fullName, Pageable pageable);
}
