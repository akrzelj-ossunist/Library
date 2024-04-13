package com.maurer.library.mapper;

import com.maurer.library.dtos.*;
import com.maurer.library.exceptions.InvalidArgumentsException;
import com.maurer.library.exceptions.ObjectDoesntExistException;
import com.maurer.library.models.Author;
import com.maurer.library.models.Book;
import com.maurer.library.models.RentEntry;
import com.maurer.library.models.User;
import com.maurer.library.services.interfaces.AuthorService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DataMapper {

    @Mapping(target = "role", expression = "java(com.maurer.library.models.enums.UserRole.USER)")
    @Mapping(target = "password", expression = "java(org.mindrot.jbcrypt.BCrypt.hashpw(userDto.getPassword(), org.mindrot.jbcrypt.BCrypt.gensalt(10)))")
    User dtoToUser(UserDto userDto);

    UserResDto userToDto(User user);

    RentEntryResDto rentEntryToDto(RentEntry rentEntry);

    Author dtoToAuthor(AuthorDto authorDto);

    AuthorResDto authorToDto(Author author);

    @Mapping(target = "isAvilable", expression = "java(true)")
    @Mapping(target = "genre", expression = "java(com.maurer.library.models.enums.Genre.valueOf(bookDto.getGenre().toUpperCase()))")
    @Mapping(target = "author", expression = "java(authorService.findByAuthorId(bookDto.getAuthor()))")
    Book dtoToBook(BookDto bookDto, AuthorService authorService) throws ObjectDoesntExistException, InvalidArgumentsException;

    BookResDto bookToDto(Book book);

    List<BookResDto> listBookToListDto(List<Book> bookList);

    List<AuthorResDto> listAuthorToListDto(List<Author> authorList);

    List<UserResDto> listUserToListDto(List<User> userList);

    List<RentEntryResDto> listRentToListDto(List<RentEntry> rentEntryList);
}
