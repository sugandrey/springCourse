package ru.sugandrey.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sugandrey.models.Book;
import ru.sugandrey.models.Person;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByPerson(Person person);
    List<Book> findByBookNameIgnoreCaseStartingWith(String bookName);

}
