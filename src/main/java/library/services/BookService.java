package library.services;

import library.models.Book;
import library.models.Person;
import library.repositories.BookRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final EntityManager entityManager;

    @Autowired
    public BookService(BookRepository bookRepository, EntityManager entityManager) {
        this.bookRepository = bookRepository;
        this.entityManager = entityManager;
    }

    public List<Book> index(Integer page, Integer booksPerPage, boolean sortByYear) {
        if (page == null || booksPerPage == null) {
            if (sortByYear) return bookRepository.findAll(Sort.by("year"));
            else return bookRepository.findAll();
        } else {
            if (sortByYear) return bookRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year")))
                    .getContent();
            else return bookRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
        }
    }

    @Transactional
    public void createNew(Book book) {
        bookRepository.save(book);
    }

    public Book showById(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Transactional
    public void edit(int id, Book book) {
        book.setId(id);
        bookRepository.save(book);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public void addUser(int id, int person_id) {
        Session session = entityManager.unwrap(Session.class);
        try (session) {
            Book book = showById(id);
            Person person = session.load(Person.class, person_id);
            book.setTakenAt(new Date());
            book.setOwner(person);
        }
    }

    public Person getUserOfBook(Book book) {
        return book.getOwner();
    }

    @Transactional
    public void setFree(int id) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) return;
        book.setTakenAt(null);
        book.setOwner(null);
    }

    public List<Book> getBooksStartsWithIgnoreCase(String search) {
        return bookRepository.findByTitleStartingWithIgnoreCase(search);
    }
}
