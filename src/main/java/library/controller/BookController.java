package library.controller;

import library.dao.BookDAO;
import library.dao.PersonDAO;
import library.model.Book;
import library.model.Person;
import library.utils.BookValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookDAO bookDAO;
    private final PersonDAO personDAO;
    private final BookValidator bookValidator;

    @Autowired
    public BookController(BookDAO bookDAO, PersonDAO personDAO, BookValidator bookValidator) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
        this.bookValidator = bookValidator;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("books", bookDAO.index());
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        Book book = bookDAO.showById(id);
        model.addAttribute("book", book);
        model.addAttribute("people", personDAO.index());
        model.addAttribute("user", bookDAO.getUserOfBook(book));
        return "books/show";
    }

    @GetMapping("/new")
    public String showCreateNew(Model model) {
        model.addAttribute("book", new Book());
        return "books/new";
    }

    @PostMapping
    public String createNew(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) return "books/new";
        bookDAO.createNew(book);
        return "redirect:/books";
    }

    @GetMapping("{id}/edit")
    public String showEdit(@PathVariable int id, Model model) {
        model.addAttribute("book", bookDAO.showById(id));
        return "/books/edit";
    }

    @PatchMapping("/{id}")
    public String edit(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book,
                       BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) return "/books/edit";
        bookDAO.edit(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookDAO.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/add_user")
    public String addUser(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        int person_id = person.getId();
        bookDAO.addUser(id, person_id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/set-free")
    public String setFree(@PathVariable("id") int id) {
        bookDAO.setFree(id);
        return "redirect:/books/" + id;
    }
}
