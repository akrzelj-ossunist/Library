package com.maurer.library.services.implementation;

import com.maurer.library.dtos.AuthorDto;
import com.maurer.library.exceptions.*;
import com.maurer.library.models.Author;
import com.maurer.library.repositories.jpa.AuthorRepository;
import com.maurer.library.services.interfaces.AuthorService;
import com.maurer.library.services.interfaces.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 *  Implementation of author service which stores data into java collection
 **/
@Service
public class AuthorServiceImplJpa implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    private final BookService bookService;

    public AuthorServiceImplJpa(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public Author addAuthor(AuthorDto authorDto) throws InvalidAuthorException, AlreadyExistException, InvalidArgumentsException, ObjectDoesntExistException {

        if(authorDto == null) throw new InvalidAuthorException("Author cannot be null!");

        //Check is already in library
        Author existingAuthor = findByFullName(authorDto.getFullName());
        if(existingAuthor != null) throw new AlreadyExistException("Author already exist in library!");

        return authorRepository.save(createAuthor(authorDto));
    }

    public Author createAuthor(AuthorDto authorDto) {

        return Author.builder()
                .fullName(authorDto.getFullName())
                .birthday(authorDto.getBirthday())
                .build();

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

        Optional<Author> author = authorRepository.findByFullName(fullName);

        if(author.isEmpty()) throw new ObjectDoesntExistException("Author you are looking for doesn't exist!");

        return author.get();
    }

    @Override
    public List<Author> findAllAuthors() {

        return authorRepository.findAll();
    }

    @Override
    public List<Author> filterAuthors(Map<String, String> allParams) throws InvalidArgumentsException {

        if(allParams.isEmpty()) throw new InvalidArgumentsException("No filter parameters provided!");

        Set<Author> filteredAuthors = new HashSet<>(authorRepository.findAll());

        allParams.forEach((key, value) -> {
            switch (key) {
                case "fullName":
                    filteredAuthors.retainAll(authorRepository.findByFullName(value).stream().toList());
                    break;
                default:
                    break;
            }
        });

        return new ArrayList<>(filteredAuthors);
    }
}
