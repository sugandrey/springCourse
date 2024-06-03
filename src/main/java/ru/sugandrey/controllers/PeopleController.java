package ru.sugandrey.controllers;


import org.springframework.beans.factory.annotation.Autowired;
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
import ru.sugandrey.models.Book;
import ru.sugandrey.models.Person;
import ru.sugandrey.services.BookService;
import ru.sugandrey.services.PersonService;
import ru.sugandrey.utils.NewPersonValidator;
import ru.sugandrey.utils.UpdatePersonValidator;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private PersonService personService;
    private NewPersonValidator newPersonValidator;
    private BookService bookService;
    private UpdatePersonValidator updatePersonValidator;

    @Autowired
    public PeopleController(
            final PersonService personService,
            final NewPersonValidator newPersonValidator,
            final BookService bookService,
            final UpdatePersonValidator updatePersonValidator
    ) {
        this.personService = personService;
        this.newPersonValidator = newPersonValidator;
        this.bookService = bookService;
        this.updatePersonValidator = updatePersonValidator;
    }


    @GetMapping()
    public String getPersonList(Model model) {
        model.addAttribute("people", personService.getPersonList());
        return "people/peopleList";
    }

    @GetMapping("/{id}")
    public String showPerson(Model model, @PathVariable("id") int id) {
        Person person = personService.showPerson(id);
        model.addAttribute("person", person);
        model.addAttribute("books", bookService.getBusyBooks(person));
        return "people/person";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id) {
        Person person = personService.showPerson(id);
        personService.deletePerson(id);
        List<Book> busyBooks = bookService.getBusyBooks(person);
        if (!busyBooks.isEmpty()) {
            for (Book busyBook : busyBooks) {
                bookService.deleteReader(busyBook.getBookId());
            }
        }
        return "redirect:/people";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping
    public String createPerson(@ModelAttribute("person") @Valid Person person, BindingResult result) {
        newPersonValidator.validate(person, result);
        if(result.hasErrors()) {
            return "people/new";
        }
        personService.createNewPerson(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String editPerson(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personService.showPerson(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String updatePerson(@ModelAttribute("person") @Valid Person person, BindingResult result, @PathVariable("id") int id) {
        updatePersonValidator.validate(person, result);
        if(result.hasErrors()) {
            return "people/edit";
        }
        personService.updatePerson(person, id);
        return "redirect:/people";
    }
}
