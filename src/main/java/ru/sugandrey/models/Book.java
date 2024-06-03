package ru.sugandrey.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookId;
    @Column(name = "book_name")
    private String bookName;
    @Column(name = "author")
    private String author;
    @Column(name = "edition_year")
    private int editionYear;

    @Column(name = "date_of_getting")
    private String dateOfGetting;
    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private Person person;

    @Transient
    private boolean isOverdue;

    public Book() {
    }

    public String getBookName() {
        return bookName;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(final int bookId) {
        this.bookId = bookId;
    }

    public void setBookName(final String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public int getEditionYear() {
        return editionYear;
    }

    public void setEditionYear(final int editionYear) {
        this.editionYear = editionYear;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(final Person person) {
        this.person = person;
    }

    public boolean isOverdue() {
        return isOverdue;
    }

    public void setOverdue(final boolean overdue) {
        isOverdue = overdue;
    }

    public String getDateOfGetting() {
        return dateOfGetting;
    }

    public void setDateOfGetting(final String dateOfGeting) {
        this.dateOfGetting = dateOfGeting;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Book book = (Book) obj;
        return this.getBookName().equals(book.getBookName());
    }
}
