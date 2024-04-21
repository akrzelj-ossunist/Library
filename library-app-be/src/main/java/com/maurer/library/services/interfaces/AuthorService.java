package com.maurer.library.services.interfaces;

import com.maurer.library.dtos.AuthorDto;
import com.maurer.library.exceptions.*;
import com.maurer.library.models.Author;
import com.maurer.library.models.Book;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *  Functionalities of author service
 **/
public interface AuthorService {

    /** Add authors in library **/
    public Author addAuthor(AuthorDto authorDto) throws InvalidAuthorException, AlreadyExistException, InvalidArgumentsException, ObjectDoesntExistException;

    /**
     * Deletes author from library
     * Cannot delete author that is assigned a book
     * **/
    public Boolean deleteAuthor(String authorId) throws InvalidAuthorException, CannotDeleteException, InvalidArgumentsException, ObjectDoesntExistException;

    /** Updates author in library **/
    public Author updateAuthor(String authorId, AuthorDto authorDto) throws InvalidArgumentsException, ObjectDoesntExistException, InvalidAuthorException, AlreadyExistException;

    /** Find author from library bz his id **/
    public Author findByAuthorId(String authorId) throws InvalidArgumentsException, ObjectDoesntExistException;

    Author findByFullName(String fullName) throws InvalidArgumentsException, ObjectDoesntExistException;

    List<Author> findByPartialFullName(String fullName) throws InvalidArgumentsException;

    /** Find all authors from library **/
    public List<Author> findAllAuthors(Map<String, String> allParams);

    List<Author> filterAuthors(Map<String, String> allParams) throws InvalidArgumentsException;

}
