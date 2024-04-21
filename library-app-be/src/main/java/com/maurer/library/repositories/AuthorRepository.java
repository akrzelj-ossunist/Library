package com.maurer.library.repositories;

import com.maurer.library.models.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, String> {

    @Query("SELECT a FROM Author a WHERE LENGTH(:fullName) >= 3 AND LOWER(a.fullName) LIKE LOWER(CONCAT('%', :fullName, '%'))")
    List<Author> findByPartialFullName(@Param("fullName") String fullName);

    Optional<Author> findByFullName(String fullName);

    Page<Author> findByFullName(String fullName, Pageable pageable);
}
