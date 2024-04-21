package com.maurer.library.services.implementation;

import com.maurer.library.dtos.AuthorDto;
import com.maurer.library.exceptions.*;
import com.maurer.library.mapper.DataMapper;
import com.maurer.library.models.Author;
import com.maurer.library.repositories.AuthorRepository;
import com.maurer.library.services.interfaces.AuthorService;
import com.maurer.library.services.interfaces.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 *  Implementation of author service which stores data into java collection
 **/
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final DataMapper dataMapper;
    private final BookService bookService;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, DataMapper dataMapper, @Lazy BookService bookService) {
        this.authorRepository = authorRepository;
        this.dataMapper = dataMapper;
        this.bookService = bookService;
    }

    @Override
    public Author addAuthor(AuthorDto authorDto) throws InvalidAuthorException, AlreadyExistException, ObjectDoesntExistException, InvalidArgumentsException {

        if(authorDto == null) throw new InvalidAuthorException("Author cannot be null!");

        //Check is already in library
        Author existingAuthor = findByFullName(authorDto.getFullName());
        if(existingAuthor != null) throw new AlreadyExistException("Author already exist in library!");

        return authorRepository.save(dataMapper.dtoToAuthor(authorDto));
    }

    @Override
    public Boolean deleteAuthor(String authorId) throws InvalidAuthorException, CannotDeleteException, InvalidArgumentsException, ObjectDoesntExistException {

        if(authorId == null) throw new InvalidAuthorException("Author cannot be null!");

        Author author = findByAuthorId(authorId);

        if(author == null) throw new InvalidAuthorException("Author doesn't exist!");

        if(!(bookService.findBooksByAuthorId(authorId)).isEmpty()) throw new CannotDeleteException("Cannot delete author because he already posses book!");

        authorRepository.delete(author);

        return true;
    }

    @Override
    public Author updateAuthor(String authorId, AuthorDto authorDto) throws InvalidArgumentsException, ObjectDoesntExistException, InvalidAuthorException, AlreadyExistException {

        if(authorDto == null || authorId == null) throw new InvalidArgumentsException("Entered invalid arguments!");

        Author author = findByAuthorId(authorId);
        if(author == null) throw new ObjectDoesntExistException("Author for selected id doesn't exist!");

        author.setFullName(authorDto.getFullName());
        author.setBirthday(authorDto.getBirthday());

        return authorRepository.save(author);
    }

    @Override
    public Author findByAuthorId(String authorId) throws InvalidArgumentsException, ObjectDoesntExistException {

        if(authorId == null) throw new InvalidArgumentsException("Entered invalid arguments!");

        Optional<Author> author = authorRepository.findById(authorId);

        if(author.isEmpty()) throw new ObjectDoesntExistException("Author you are looking for doesn't exist!");

        return author.get();
    }

    @Override
    public Author findByFullName(String fullName) throws InvalidArgumentsException, ObjectDoesntExistException {

        if(fullName == null) throw new InvalidArgumentsException("Entered invalid arguments!");

        return authorRepository.findByFullName(fullName).orElse(null);
    }

    @Override
    public List<Author> findByPartialFullName(String fullName) throws InvalidArgumentsException {

        if(fullName == null) throw new InvalidArgumentsException("Entered invalid arguments!");

        return authorRepository.findByPartialFullName(fullName);
    }

    @Override
    public List<Author> findAllAuthors(Map<String, String> allParams) {

        int page = allParams.get("page") != null ? Integer.parseInt(allParams.get("page")) - 1 : 0;
        int size = allParams.get("size") != null ? Integer.parseInt(allParams.get("size")) : 10;

        Pageable pageable = (Pageable) PageRequest.of(page, size);

        return authorRepository.findAll(pageable).getContent();
    }

    @Override
    public List<Author> filterAuthors(Map<String, String> allParams) throws InvalidArgumentsException {

        int page = allParams.get("page") != null ? Integer.parseInt(allParams.get("page")) - 1 : 0;
        int size = allParams.get("size") != null ? Integer.parseInt(allParams.get("size")) : 10;

        Pageable pageable = (Pageable) PageRequest.of(page, size);

        return authorRepository.findByFullName(allParams.get("fullName"), pageable).getContent();
    }
}