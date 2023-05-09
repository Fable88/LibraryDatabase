package library.services;

import library.models.Book;
import library.models.Person;
import library.repositories.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> index() {
        return peopleRepository.findAll();
    }

    @Transactional
    public void createNew(Person person) {
        peopleRepository.save(person);
    }

    public boolean isNameExisting(String name) {
        return peopleRepository.existsByName(name);
    }

    public Person showById(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    @Transactional
    public void update(int id, Person person) {
        person.setId(id);
        peopleRepository.save(person);
    }

    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public List<Book> getBooksOfPersonById(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());
            return person.get().getBooks();
        }
        return Collections.emptyList();
    }
}
