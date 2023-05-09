package library.controller;

import library.models.Book;
import library.models.Person;
import library.services.BookService;
import library.services.PeopleService;
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

    private final BookValidator bookValidator;
    private final BookService bookService;
    private final PeopleService peopleService;

    @Autowired
    public BookController(BookValidator bookValidator, BookService bookService, PeopleService peopleService) {
        this.bookValidator = bookValidator;
        this.bookService = bookService;
        this.peopleService = peopleService;
    }

    @GetMapping
    public String index(@RequestParam(name = "page", required = false) Integer page,
                        @RequestParam(name = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(name = "sort_by_year", defaultValue = "false") boolean sortByYear,
                        Model model) {
        model.addAttribute("books", bookService.index(page, booksPerPage, sortByYear));
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        Book book = bookService.showById(id);
        model.addAttribute("book", book);
        model.addAttribute("people", peopleService.index());
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
        bookService.createNew(book);
        return "redirect:/books";
    }

    @GetMapping("{id}/edit")
    public String showEdit(@PathVariable int id, Model model) {
        model.addAttribute("book", bookService.showById(id));
        return "/books/edit";
    }

    @PatchMapping("/{id}")
    public String edit(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book,
                       BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) return "/books/edit";
        bookService.edit(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/add_user")
    public String addUser(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        int person_id = person.getId();
        bookService.addUser(id, person_id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/set-free")
    public String setFree(@PathVariable("id") int id) {
        bookService.setFree(id);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String showSearchPage(@RequestParam(name = "request", required = false) String search, Model model) {
        if (search == null) return "/books/search";
        model.addAttribute("found", bookService.getBooksStartsWithIgnoreCase(search));
        return "/books/search";
    }
}
