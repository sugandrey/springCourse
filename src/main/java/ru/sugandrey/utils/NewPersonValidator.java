package ru.sugandrey.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.sugandrey.models.Person;
import ru.sugandrey.services.PersonService;

import java.time.Year;

@Component
public class NewPersonValidator implements Validator {

    private PersonService personService;
    private static final int BIRTH_YEAR_MIN = 1920;
    private static final int BIRTH_YEAR_MAX = Year.now().getValue();

    @Autowired
    public NewPersonValidator(final PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;
        if (person.getFullName().isEmpty()) {
            errors.rejectValue("fullName", "", "!!!ФИО не должно быть пустым!!!");
        }
        if (person.getBirthYear() < BIRTH_YEAR_MIN || person.getBirthYear() >= BIRTH_YEAR_MAX) {
            errors.rejectValue("birthYear", "", "!!!Дата рождения нереальна!!!");
        }
        if (personService.checkFIODuplicates(person)) {
            errors.rejectValue("fullName", "", "!!!ФИО уже существует!!!");
        }
    }
}
