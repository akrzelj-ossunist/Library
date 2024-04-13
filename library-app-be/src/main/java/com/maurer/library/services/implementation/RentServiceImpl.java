package com.maurer.library.services.implementation;

import com.maurer.library.dtos.RentBookDto;
import com.maurer.library.exceptions.*;
import com.maurer.library.models.Book;
import com.maurer.library.models.RentEntry;
import com.maurer.library.models.User;
import com.maurer.library.models.enums.Status;
import com.maurer.library.repositories.RentRepository;
import com.maurer.library.services.interfaces.BookService;
import com.maurer.library.services.interfaces.RentService;
import com.maurer.library.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 *  Implementation of rent entry service which stores data into java collection
 **/
@Service
public class RentServiceImpl implements RentService {

    private final RentRepository rentRepository;
    private final UserService userService;
    private final BookService bookService;

    @Autowired
    public RentServiceImpl(RentRepository rentRepository, UserService userService, BookService bookService) {
        this.rentRepository = rentRepository;
        this.userService = userService;
        this.bookService = bookService;
    }


    @Override
    public RentEntry rentBook(RentBookDto rentBookDto) throws ObjectDoesntExistException, InvalidArgumentsException, CurrentlyUnavailableException, AlreadyExistException {

        if(rentBookDto == null) throw new InvalidArgumentsException("Sent arguments are invalid!");

        Book book = bookService.findBookById(rentBookDto.getBookId());
        if(book == null) throw new ObjectDoesntExistException("Object that you were looking for doesn't exist!");
        if(!book.getIsAvilable()) throw new CurrentlyUnavailableException("Book you wanna rent is currently unavailable!");

        User user = userService.findUserById(rentBookDto.getUserId());
        if(user == null) throw new ObjectDoesntExistException("Object that you were looking for doesn't exist!");

        return rentRepository.save(createRentEntry(user, book));
    }

    public RentEntry createRentEntry(User user, Book book) {

        return RentEntry.builder()
                .book(book)
                .user(user)
                .status(Status.RENTED)
                .lendingDate(new Date())
                .build();
    }

    @Override
    public RentEntry returnBook(String rentEntryId, String note) throws ObjectDoesntExistException, InvalidArgumentsException, AlreadyExistException {

        if(rentEntryId == null) throw new InvalidArgumentsException("Sent arguments are invalid!");

        Optional<RentEntry> rentEntry = rentRepository.findById(rentEntryId);

        if(rentEntry.isEmpty()) throw new ObjectDoesntExistException("Object that you were looking for doesn't exist!");

        rentEntry.get().setStatus(Status.RETURNED);
        rentEntry.get().setNote(note);
        rentEntry.get().setReturnDate(new Date());

        return rentRepository.save(rentEntry.get());
    }

    @Override
    public RentEntry findRentEntryById(String rentEntryId) throws InvalidArgumentsException, ObjectDoesntExistException {

        if (rentEntryId == null) throw new InvalidArgumentsException("Sent arguments cannot be null!");

        Optional<RentEntry> rentEntry = rentRepository.findById(rentEntryId);

        if (rentEntry.isEmpty()) throw new ObjectDoesntExistException("Rent entry you are looking for doesn't exist!");

        return rentEntry.get();
    }

    @Override
    public List<RentEntry> findRentsByUserId(String userId) throws InvalidArgumentsException, ObjectDoesntExistException {

        if(userId == null) throw new InvalidArgumentsException("Entered user id cannot be null");

        return rentRepository.findByUser(userService.findUserById(userId));
    }

    @Override
    public List<RentEntry> findRentsByBookId(String bookId) throws InvalidArgumentsException, ObjectDoesntExistException {

        if(bookId == null) throw new InvalidArgumentsException("Entered book id cannot be null");

        return rentRepository.findByBook(bookService.findBookById(bookId));
    }

    @Override
    public List<RentEntry> findAllRentEntries(Map<String, String> allParams) {

        int page = allParams.get("page") != null ? Integer.parseInt(allParams.get("page")) - 1 : 0;
        int size = allParams.get("size") != null ? Integer.parseInt(allParams.get("size")) : 10;

        Pageable pageable = (Pageable) PageRequest.of(page, size);

        List<RentEntry> rentEntryList = rentRepository.findAll(pageable).getContent();

        return setExpiredRentEntries(rentEntryList);
    }

    @Override
    public List<RentEntry> findAllRentEntriesByStatus(String status) throws InvalidArgumentsException, InvalidEnumException {

        if(status == null) throw new InvalidArgumentsException("Entered status cannot be null");

        return rentRepository.findByStatus(status);
    }

    @Override
    public List<RentEntry> setExpiredRentEntries(List<RentEntry> rentEntryList) {

        List<RentEntry> rentEntryListChecked = new ArrayList<>();

        rentEntryList.forEach(entry -> {

            Date lendingDate = entry.getLendingDate();

            // Convert the lending date type of Date to LocalDate
            LocalDate lendingLocalDate = lendingDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // Get the current date
            LocalDate currentDate = LocalDate.now();

            // Calculate the difference in days between the current date and the lending date
            long daysDifference = ChronoUnit.DAYS.between(lendingLocalDate, currentDate);

            // Check if the difference is greater than 30 days and status is not already expired
            if(daysDifference > 30 && entry.getStatus() != Status.EXPIRED) {
              entry.setStatus(Status.EXPIRED);
              rentRepository.save(entry);
              rentEntryListChecked.add(entry);
            }
        });
        return  rentEntryListChecked;
    }

    public List<RentEntry> filterRentEntries(Map<String, String> allParams) throws ObjectDoesntExistException, InvalidArgumentsException {

        int page = allParams.get("page") != null ? Integer.parseInt(allParams.get("page")) - 1 : 0;
        int size = allParams.get("size") != null ? Integer.parseInt(allParams.get("size")) : 10;

        Pageable pageable = (Pageable) PageRequest.of(page, size);

        User user = userService.findUserById(allParams.get("user"));

        Book book = bookService.findBookByTitle(allParams.get("book"));

        String status = allParams.get("status");

        String lendingDateString = allParams.get("lendingDate");
        String returnDateString = allParams.get("returnDate");

        // Convert string representations to Date objects
        Date lendingDate = null;
        Date returnDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // You can change the format as per your input

        try {
            if (lendingDateString != null) {
                lendingDate = dateFormat.parse(lendingDateString);
            }
            if (returnDateString != null) {
                returnDate = dateFormat.parse(returnDateString);
            }
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        List<RentEntry> rentEntryList = rentRepository.findByUserAndBookAndStatusAndLendingDateAndReturnDate(user, book, status, lendingDate, returnDate, pageable).getContent();

        return setExpiredRentEntries(rentEntryList);
    }
}
