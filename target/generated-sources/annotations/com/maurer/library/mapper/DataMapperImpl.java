package com.maurer.library.mapper;

import com.maurer.library.dtos.AuthorDto;
import com.maurer.library.dtos.AuthorResDto;
import com.maurer.library.dtos.BookDto;
import com.maurer.library.dtos.BookResDto;
import com.maurer.library.dtos.RentEntryResDto;
import com.maurer.library.dtos.UserDto;
import com.maurer.library.dtos.UserResDto;
import com.maurer.library.exceptions.InvalidArgumentsException;
import com.maurer.library.exceptions.ObjectDoesntExistException;
import com.maurer.library.models.Author;
import com.maurer.library.models.Book;
import com.maurer.library.models.RentEntry;
import com.maurer.library.models.User;
import com.maurer.library.services.interfaces.AuthorService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-21T11:38:25+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Private Build)"
)
@Component
public class DataMapperImpl implements DataMapper {

    @Override
    public User dtoToUser(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.fullName( userDto.getFullName() );
        user.email( userDto.getEmail() );
        user.address( userDto.getAddress() );
        user.birthday( userDto.getBirthday() );

        user.role( com.maurer.library.models.enums.UserRole.USER );
        user.password( org.mindrot.jbcrypt.BCrypt.hashpw(userDto.getPassword(), org.mindrot.jbcrypt.BCrypt.gensalt(10)) );

        return user.build();
    }

    @Override
    public UserResDto userToDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserResDto.UserResDtoBuilder userResDto = UserResDto.builder();

        userResDto.id( user.getId() );
        userResDto.fullName( user.getFullName() );
        userResDto.email( user.getEmail() );
        userResDto.address( user.getAddress() );
        userResDto.birthday( user.getBirthday() );
        userResDto.role( user.getRole() );

        return userResDto.build();
    }

    @Override
    public RentEntryResDto rentEntryToDto(RentEntry rentEntry) {
        if ( rentEntry == null ) {
            return null;
        }

        RentEntryResDto.RentEntryResDtoBuilder rentEntryResDto = RentEntryResDto.builder();

        rentEntryResDto.id( rentEntry.getId() );
        rentEntryResDto.lendingDate( rentEntry.getLendingDate() );
        rentEntryResDto.returnDate( rentEntry.getReturnDate() );
        rentEntryResDto.status( rentEntry.getStatus() );
        rentEntryResDto.note( rentEntry.getNote() );
        rentEntryResDto.book( rentEntry.getBook() );
        rentEntryResDto.user( rentEntry.getUser() );

        return rentEntryResDto.build();
    }

    @Override
    public Author dtoToAuthor(AuthorDto authorDto) {
        if ( authorDto == null ) {
            return null;
        }

        Author.AuthorBuilder author = Author.builder();

        author.fullName( authorDto.getFullName() );
        author.birthday( authorDto.getBirthday() );

        return author.build();
    }

    @Override
    public AuthorResDto authorToDto(Author author) {
        if ( author == null ) {
            return null;
        }

        AuthorResDto.AuthorResDtoBuilder authorResDto = AuthorResDto.builder();

        authorResDto.id( author.getId() );
        authorResDto.fullName( author.getFullName() );
        authorResDto.birthday( author.getBirthday() );

        return authorResDto.build();
    }

    @Override
    public Book dtoToBook(BookDto bookDto, AuthorService authorService) throws ObjectDoesntExistException, InvalidArgumentsException {
        if ( bookDto == null && authorService == null ) {
            return null;
        }

        Book.BookBuilder book = Book.builder();

        if ( bookDto != null ) {
            book.title( bookDto.getTitle() );
            book.note( bookDto.getNote() );
            book.isbn( bookDto.getIsbn() );
        }
        book.isAvilable( true );
        book.genre( com.maurer.library.models.enums.Genre.valueOf(bookDto.getGenre().toUpperCase()) );
        book.author( authorService.findByAuthorId(bookDto.getAuthor()) );

        return book.build();
    }

    @Override
    public BookResDto bookToDto(Book book) {
        if ( book == null ) {
            return null;
        }

        BookResDto.BookResDtoBuilder bookResDto = BookResDto.builder();

        bookResDto.id( book.getId() );
        bookResDto.title( book.getTitle() );
        bookResDto.author( book.getAuthor() );
        bookResDto.isAvilable( book.getIsAvilable() );
        bookResDto.note( book.getNote() );
        bookResDto.createdDate( book.getCreatedDate() );
        bookResDto.isbn( book.getIsbn() );
        bookResDto.genre( book.getGenre() );

        return bookResDto.build();
    }

    @Override
    public List<BookResDto> listBookToListDto(List<Book> bookList) {
        if ( bookList == null ) {
            return null;
        }

        List<BookResDto> list = new ArrayList<BookResDto>( bookList.size() );
        for ( Book book : bookList ) {
            list.add( bookToDto( book ) );
        }

        return list;
    }

    @Override
    public List<AuthorResDto> listAuthorToListDto(List<Author> authorList) {
        if ( authorList == null ) {
            return null;
        }

        List<AuthorResDto> list = new ArrayList<AuthorResDto>( authorList.size() );
        for ( Author author : authorList ) {
            list.add( authorToDto( author ) );
        }

        return list;
    }

    @Override
    public List<UserResDto> listUserToListDto(List<User> userList) {
        if ( userList == null ) {
            return null;
        }

        List<UserResDto> list = new ArrayList<UserResDto>( userList.size() );
        for ( User user : userList ) {
            list.add( userToDto( user ) );
        }

        return list;
    }

    @Override
    public List<RentEntryResDto> listRentToListDto(List<RentEntry> rentEntryList) {
        if ( rentEntryList == null ) {
            return null;
        }

        List<RentEntryResDto> list = new ArrayList<RentEntryResDto>( rentEntryList.size() );
        for ( RentEntry rentEntry : rentEntryList ) {
            list.add( rentEntryToDto( rentEntry ) );
        }

        return list;
    }
}
