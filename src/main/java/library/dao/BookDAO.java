package library.dao;

import library.model.Book;
import library.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index() {
        return jdbcTemplate.query("SELECT * FROM books", new BeanPropertyRowMapper<>(Book.class));
    }

    public void createNew(Book book) {
        jdbcTemplate.update("INSERT INTO books(title, author, year) VALUES (?, ?, ?)",
                book.getTitle(), book.getAuthor(), book.getYear());
    }

    public Book showById(int id) {
        return jdbcTemplate.query("SELECT * FROM books WHERE id = ?", new Object[]{id},
                new BeanPropertyRowMapper<>(Book.class)).stream().findFirst().orElse(null);
    }

    public void edit(int id, Book book) {
        jdbcTemplate.update("UPDATE books SET title = ?, author = ?, year = ? WHERE id = ?",
                book.getTitle(), book.getAuthor(), book.getYear(), book.getId());
    }

    public List<Book> getBooksOfPerson(Person person) {
        int person_id = person.getId();
        return jdbcTemplate.query("SELECT * FROM books WHERE person_id = ?", new Object[]{person_id},
                new BeanPropertyRowMapper<>(Book.class));
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM books WHERE id = ?", id);
    }

    public void addUser(int id, int person_id) {
        jdbcTemplate.update("UPDATE books SET person_id = ? WHERE id = ?", person_id, id);
    }

    public Person getUserOfBook(Book book) {
        return jdbcTemplate.query("SELECT * FROM people WHERE id = ?",
                        new Object[]{book.getPerson_id()}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findFirst().orElse(null);
    }

    public void setFree(int id) {
        jdbcTemplate.update("UPDATE books SET person_id = null WHERE id = ?", id);
    }
}
