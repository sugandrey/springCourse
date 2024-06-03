package ru.sugandrey.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sugandrey.models.Book;
import ru.sugandrey.models.Person;
import ru.sugandrey.services.BookService;
import ru.sugandrey.services.PersonService;
import ru.sugandrey.utils.NewBookValidator;
import ru.sugandrey.utils.UpdateBookValidator;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    private final PersonService personService;
    private final BookService bookService;
    private final NewBookValidator newBookValidator;
    private final UpdateBookValidator updateBookValidator;

    @Autowired
    public BookController(final BookService bookService,
                          final PersonService personService,
                          final NewBookValidator newBookValidator,
                          final UpdateBookValidator updateBookValidator) {
        this.bookService = bookService;
        this.newBookValidator = newBookValidator;
        this.personService = personService;
        this.updateBookValidator = updateBookValidator;

    }

    @GetMapping()
    public String getBookList(
            final Model model,
            @RequestParam(value = "page", required = false) final Integer pageNumber,
            @RequestParam(value = "books_per_page", required = false) final Integer booksCount,
            @RequestParam(value = "sort_by_year", required = false) final boolean sortByYear) {
        final List<Book> fullList = bookService.getBookList(sortByYear, booksCount, pageNumber);
//        if (pageNumber != null && booksCount != null) {
//           final List<Book> splitedBooksList = bookService.getSplitedBooksList(fullList, booksCount, pageNumber);
//           if (splitedBooksList.isEmpty()) {
//               return "books/emptyBookPage";
//           }
//            model.addAttribute("books", splitedBooksList);
//        }
//        else {
            model.addAttribute("books", fullList);
//        }
        return "books/bookList";
    }

    @GetMapping("/{id}")
    public String showBook(final Model model, @PathVariable("id") final int id) {
        final Book book = bookService.showBook(id);
        model.addAttribute("book", book);
        model.addAttribute("people", personService.getPersonList());
        model.addAttribute("person", book.getPerson());
        return "books/book";
    }

    @GetMapping("/search")
    public String search() {
            return "books/search";
    }

    @PostMapping("/search")
    public String search(@RequestParam("keyword") @Nullable final String keyword, final Model model) {
        model.addAttribute("books", bookService.findByBookNameStartingWith(keyword));
        return "books/search";
    }

    @PostMapping("/{id}")
    public String addReader(@ModelAttribute("person") final Person person, @PathVariable("id") final int bookId) {
        bookService.addReader(bookId, person);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/0")
    public String deleteReader(@PathVariable("id") final int bookId) {
        bookService.deleteReader(bookId);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") final int id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") final Book book) {
        return "books/new";
    }

    @PostMapping
    public String createBook(@ModelAttribute("book") @Valid final Book book, final BindingResult result) {
        newBookValidator.validate(book, result);
        if(result.hasErrors()) {
            return "books/new";
        }
        bookService.createNewBook(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(final Model model, @PathVariable("id") final int id) {
        model.addAttribute("book", bookService.showBook(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String updateBook(@ModelAttribute("book") final Book book, final BindingResult result, @PathVariable("id")
    final int id) {
        updateBookValidator.validate(book, result);
        if(result.hasErrors()) {
            return "books/edit";
        }
        bookService.updateBook(book, id);
        return "redirect:/books";
    }
}
