package ru.sugandrey.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.sugandrey.models.Book;
import ru.sugandrey.services.BookService;

@Component
public class NewBookValidator implements Validator {

    private BookService bookService;
    private UpdateBookValidator updateBookValidator;

    @Autowired
    public NewBookValidator(final BookService bookService, final UpdateBookValidator updateBookValidator) {
        this.bookService = bookService;
        this.updateBookValidator = updateBookValidator;
    }


    @Override
    public boolean supports(Class<?> aClass) {
        return Book.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Book book = (Book) o;
        updateBookValidator.validate(o, errors);
        if (bookService.checkBookDuplicates(book)) {
            errors.rejectValue("bookName", "", "!!!Эта книга уже есть в библиотеке!!!");
        }
    }
}
