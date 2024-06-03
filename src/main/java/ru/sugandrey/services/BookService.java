package ru.sugandrey.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;
import ru.sugandrey.models.Book;
import ru.sugandrey.models.Person;
import ru.sugandrey.repositories.BookRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(final BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getBookList(final boolean sortedByYear, final Integer booksOnPageCount, final Integer pageNumber) {
        if (sortedByYear && booksOnPageCount == null && pageNumber == null) {
            return bookRepository.findAll(Sort.by("editionYear"));
        }
        else if (!sortedByYear && booksOnPageCount != null && pageNumber != null) {
            return bookRepository.findAll(PageRequest.of(pageNumber, booksOnPageCount)).getContent();
        } else if (sortedByYear && booksOnPageCount != null && pageNumber != null) {
            return bookRepository.findAll(PageRequest.of(pageNumber, booksOnPageCount, Sort.by("editionYear"))).getContent();
        }
        else {
            return bookRepository.findAll();
        }
    }

    public Book showBook(int id) {
        final Optional<Book> foundBook = bookRepository.findById(id);
        return foundBook.orElse(null);
    }

    @Transactional
    public void createNewBook(final Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void updateBook(final Book updatedBook, int id) {
        final Book bookToBeUpdated = bookRepository.findById(id).get();
       updatedBook.setBookId(id);
       updatedBook.setPerson(bookToBeUpdated.getPerson());
       updatedBook.setDateOfGetting(bookToBeUpdated.getDateOfGetting());
       bookRepository.save(updatedBook);
    }

    @Transactional
    public void deleteBook(final int id) {
        bookRepository.deleteById(id);
    }

    public List<Book> getBusyBooks(final Person person) {

        final List<Book> books = bookRepository.findByPerson(person);
        for (final Book book : books) {
            book.setOverdue(isOverdue(book.getDateOfGetting()));
        }
        return books;
    }

    public boolean checkBookDuplicates(final Book book) {
        return getBookList(false, null, null).contains(book);
    }

    @Transactional
    public void addReader(final int bookId, final Person person) {
        final Optional<Book> foundBook = bookRepository.findById(bookId);
        if (foundBook.isPresent()) {
            foundBook.get().setPerson(person);
            foundBook.get().setDateOfGetting(convertDateToString());
            bookRepository.save(foundBook.get());
        }
        else {
            throw new UnexpectedRollbackException("!!!Такой книги не существует!!!");
        }
    }

    @Transactional
    public void deleteReader(final int bookId) {
        final Optional<Book> foundBook = bookRepository.findById(bookId);
        if (foundBook.isPresent()) {
            foundBook.get().setPerson(null);
            foundBook.get().setDateOfGetting(null);
//            bookRepository.save(foundBook.get());
        }
        else {
            throw new UnexpectedRollbackException("!!!Такой книги не существует!!!");
        }
    }

    public List<Book> getSplitedBooksList(final List<Book> books, final Integer booksOnPageCount, final Integer pageNumber) {
        if ((int) Math.ceil((double) books.size()/booksOnPageCount) <= pageNumber) {
            return Collections.emptyList();
        }
        final List<List<Book>> subLists = new ArrayList<>();
        final int booksSize = books.size();
        for (int i = 0; i < booksSize; i+=booksOnPageCount) {
            subLists.add(books.subList(i, Math.min(booksOnPageCount + i, booksSize)));
        }
        return subLists.get(pageNumber);
    }

    public List<Book> findByBookNameStartingWith(@Nullable String bookName) {
        if (bookName == null || bookName.isEmpty()) {
            return new ArrayList<>();
        }
        bookName = bookName.substring(0, 1).toUpperCase() + bookName.substring(1);
        final List<Book> foundBooks = bookRepository.findByBookNameIgnoreCaseStartingWith(bookName);
        if (foundBooks.isEmpty()) {
            return Collections.emptyList();
        }
        System.out.println("Найденные книги " + foundBooks.get(0).getBookName());
        return foundBooks;
    }

    private String convertDateToString() {
        return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private LocalDate convertStringToDate(final String date) {
        return LocalDate.parse(date);
    }

    private boolean isOverdue(@Nullable final String date) {
        if (date != null) {
            final LocalDate bookGettingDate = convertStringToDate(date);
            return LocalDate.now().minusDays(10L).compareTo(bookGettingDate) > 0;
        }
        else {
            return false;
        }
    }
}
