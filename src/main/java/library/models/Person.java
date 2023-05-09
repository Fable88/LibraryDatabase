package library.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Person")
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "Name should not be empty.")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters.")
    private String name;

    @Column(name = "birthday")
    @NotNull(message = "Date should not be empty.")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date birthday;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    public Person(int id, String name, Date birthday) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
    }

    public Person() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public String getFormattedBirthday() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        return formatter.format(birthday);
    }
}
