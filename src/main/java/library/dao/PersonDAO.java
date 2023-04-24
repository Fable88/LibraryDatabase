package library.dao;

import library.model.Person;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM people", new BeanPropertyRowMapper<>(Person.class));
    }

    public void createNew(Person person) {
        jdbcTemplate.update("INSERT INTO people(name, birthday) VALUES (?, ?)",
                person.getName(), person.getBirthday());
    }

    public boolean isNameExisting(String name) {
        return jdbcTemplate.query("SELECT * FROM people WHERE name = ?",
                new Object[]{name}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny().isPresent();
    }

    public Person showById(int id) {
        return jdbcTemplate.query("SELECT * FROM people WHERE id = ?",
                new Object[]{id}, new BeanPropertyRowMapper<>(Person.class)).stream().findFirst().orElse(null);
    }

    public void update(int id, Person person) {
        jdbcTemplate.update("UPDATE people SET name = ?, birthday = ? WHERE id = ?",
                person.getName(), person.getBirthday(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM people WHERE id = ?", id);
    }
}
