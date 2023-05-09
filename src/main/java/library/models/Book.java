package library.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    @NotEmpty(message = "Title should not be empty.")
    private String title;

    @Column(name = "author")
    @NotEmpty(message = "Author should not be empty.")
    private String author;

    @Column(name = "year")
    @NotEmpty(message = "Year should not be empty.")
    @Size(min = 1, max = 4)
    private String year;

    public void setTakenAt(Date takenAt) {
        this.takenAt = takenAt;
    }

    @Column(name = "taken_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date takenAt;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    @Transient
    private boolean isOverdue;

    public Book(String title, String author, String year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public Book() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Date getTakenAt() {
        return takenAt;
    }

    public boolean isOverdue() {
        Date now = new Date();

        long diffInMillis = Math.abs(now.getTime() - takenAt.getTime());
        long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        return diffInDays > 10;
    }

    public void setOverdue(boolean overdue) {
        isOverdue = overdue;
    }
}
