package com.maurer.library.utils;

import com.maurer.library.dtos.AuthorResDto;
import com.maurer.library.dtos.BookResDto;
import com.maurer.library.dtos.RentEntryResDto;
import com.maurer.library.dtos.UserResDto;
import com.maurer.library.models.Author;
import com.maurer.library.models.Book;
import com.maurer.library.models.RentEntry;
import com.maurer.library.models.User;

import java.util.ArrayList;
import java.util.List;

public class ConvertModel {

    public static AuthorResDto convertAuthor(Author author) {
        return AuthorResDto.builder()
                .id(author.getId())
                .fullName(author.getFullName())
                .birthday(author.getBirthday())
                .build();
    }

    public static List<AuthorResDto> convertAuthorList(List<Author> authors) {
        List<AuthorResDto> authorResDto = new ArrayList<>();

        authors.forEach(author -> authorResDto.add(convertAuthor(author)));

        return authorResDto;
    }

    public static BookResDto convertBook(Book book) {
        return BookResDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isAvilable(book.getIsAvilable())
                .note(book.getNote())
                .createdDate(book.getCreatedDate())
                .isbn(book.getIsbn())
                .genre(book.getGenre())
                .build();
    }

    public static List<BookResDto> convertBookList(List<Book> books) {
        List<BookResDto> bookResDto = new ArrayList<>();

        books.forEach(book -> bookResDto.add(convertBook(book)));

        return bookResDto;
    }

    public static RentEntryResDto convertRentEntry(RentEntry rentEntry) {
        return RentEntryResDto.builder()
                .id(rentEntry.getId())
                .lendingDate(rentEntry.getLendingDate())
                .returnDate(rentEntry.getReturnDate())
                .status(rentEntry.getStatus())
                .note(rentEntry.getNote())
                .book(rentEntry.getBook())
                .user(rentEntry.getUser())
                .build();
    }

    public static List<RentEntryResDto> convertRentEntryList(List<RentEntry> rentEntryList) {
        List<RentEntryResDto> rentEntryResDto  = new ArrayList<>();

        rentEntryList.forEach(rentEntry -> rentEntryResDto.add(convertRentEntry(rentEntry)));

        return rentEntryResDto;
    }

    public static UserResDto convertUser(User user) {
        return UserResDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .address(user.getAddress())
                .birthday(user.getBirthday())
                .role(user.getRole())
                .build();
    }

    public static List<UserResDto> convertUserList(List<User> users) {
        List<UserResDto> userResDto  = new ArrayList<>();

        users.forEach(user -> userResDto.add(convertUser(user)));

        return userResDto;
    }
}
