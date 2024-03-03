package com.maurer.library.controllers.implementation;

import com.maurer.library.aspect.LoggerUtil;
import com.maurer.library.controllers.interfaces.AuthorController;
import com.maurer.library.dtos.AuthorDto;
import com.maurer.library.dtos.AuthorResDto;
import com.maurer.library.exceptions.*;
import com.maurer.library.models.Author;
import com.maurer.library.services.interfaces.AuthorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.maurer.library.utils.ConvertModel.convertAuthor;
import static com.maurer.library.utils.ConvertModel.convertAuthorList;

@RestController
@RequestMapping("/author")
@CrossOrigin
public class AuthorControllerImpl implements AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorControllerImpl(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Override
    @PostMapping("/create")
    public ResponseEntity<AuthorResDto> create(@Valid @RequestBody AuthorDto authorDto) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException, InvalidAuthorException {

        Author createdAuthor = authorService.addAuthor(authorDto);

        LoggerUtil.logInfo("New author created: " + createdAuthor.getFullName());

        return ResponseEntity.status(HttpStatus.CREATED).body(convertAuthor(createdAuthor));

    }

    @Override
    @PutMapping("/update/{id}")
    public ResponseEntity<AuthorResDto> edit(@PathVariable("id") String authorId, @Valid @RequestBody AuthorDto authorDto) throws ObjectDoesntExistException, AlreadyExistException, InvalidArgumentsException, InvalidAuthorException {

        Author updatedAuthor = authorService.updateAuthor(authorId, authorDto);

        LoggerUtil.logInfo("Existing author updated: " + updatedAuthor.getFullName());

        return ResponseEntity.ok().body(convertAuthor(updatedAuthor));
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") String authorId) throws CannotDeleteException, ObjectDoesntExistException, InvalidArgumentsException, InvalidAuthorException {

        boolean deleted = authorService.deleteAuthor(authorId);

        LoggerUtil.logInfo("Author successfully deleted!");

        return ResponseEntity.ok().body(deleted);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResDto> authorProfile(@PathVariable("id") String authorId) throws ObjectDoesntExistException, InvalidArgumentsException {

        Author author = authorService.findByAuthorId(authorId);

        LoggerUtil.logInfo("Authors page successfully fetched! " + author.getFullName());

        return ResponseEntity.ok().body(convertAuthor(author));
    }

    @Override
    @GetMapping("/list")
    public ResponseEntity<List<AuthorResDto>> list() {

        List<Author> authors = authorService.findAllAuthors();

        LoggerUtil.logInfo("List of Authors successfully fetched!");

        return ResponseEntity.ok().body(convertAuthorList(authors));
    }

    @Override
    @GetMapping("/query")
    public ResponseEntity<List<AuthorResDto>> filterList(@RequestParam Map<String, String> allParams) throws InvalidArgumentsException {

        if (allParams.isEmpty()) throw new InvalidArgumentsException("Sent arguments cannot be null!");

        List<Author> filteredAuthors = authorService.filterAuthors(allParams);

        LoggerUtil.logInfo("List of filtered Authors successfully fetched!");

        return ResponseEntity.ok().body(convertAuthorList(filteredAuthors));
    }
}
