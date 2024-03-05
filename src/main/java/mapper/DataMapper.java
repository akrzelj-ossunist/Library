package mapper;

import com.maurer.library.dtos.*;
import com.maurer.library.models.Author;
import com.maurer.library.models.Book;
import com.maurer.library.models.RentEntry;
import com.maurer.library.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DataMapper {

    User dtoToUser(UserDto userDto);
    UserResDto userToDto(User user);
    RentEntryResDto rentEntryToDto(RentEntry rentEntry);
    Author dtoToAuthor(AuthorDto authorDto);
    AuthorResDto authorToDto(Author author);
    Book dtoToBook(BookDto bookDto);
    BookResDto bookToDto(Book book);
}
