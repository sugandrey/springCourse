package ru.sugandrey.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Table(name = "people")
public class Person {

    @Id
    @Column(name = "person_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int personId;
    @NotEmpty(message = "ФИО не может быть пустым!!!")
    @Column(name = "full_name")
    private String fullName;
    @Min(1)
    @Column(name = "birth_year")
    private int birthYear;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private List<Book> books;


    public Person() {
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(final int personId) {
        this.personId = personId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(final String fullName) {
        this.fullName = fullName;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(final int birthYear) {
        this.birthYear = birthYear;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(final List<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Person person = (Person) obj;
       return this.getFullName().equals(person.getFullName());
    }
}
