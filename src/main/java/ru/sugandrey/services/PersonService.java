package ru.sugandrey.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sugandrey.models.Person;
import ru.sugandrey.repositories.PeopleRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PersonService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonService(final PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> getPersonList() {
        return peopleRepository.findAll();
    }

    public Person showPerson(int id) {
        final Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElse(null);
    }

    @Transactional
    public void createNewPerson(final Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void updatePerson(final Person updatedPerson, final int id) {
        updatedPerson.setPersonId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void deletePerson(final int id) {
        peopleRepository.deleteById(id);
    }

    public boolean checkFIODuplicates(final Person person) {
        return getPersonList().contains(person);
    }

}
