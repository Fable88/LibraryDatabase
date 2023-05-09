package library.controller;

import library.models.Person;
import library.services.BookService;
import library.services.PeopleService;
import library.utils.PersonValidator;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PersonController {

    private final PeopleService peopleService;
    private final BookService bookService;
    private final PersonValidator personValidator;

    @Autowired
    public PersonController(PeopleService peopleService, BookService bookService, PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.bookService = bookService;
        this.personValidator = personValidator;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("people", peopleService.index());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Person person = peopleService.showById(id);
        model.addAttribute("person", person);
        model.addAttribute("books", peopleService.getBooksOfPersonById(person.getId()));
        return "/people/show";
    }

    @GetMapping("/new")
    public String showCreateNew(Model model) {
        model.addAttribute("person", new Person());
        return "people/new";
    }

    @PostMapping
    public String createNew(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) return "people/new";
        peopleService.createNew(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String showEdit(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.showById(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String edit(@PathVariable("id") int id, @ModelAttribute("person") @Valid Person person,
                       BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) return "people/edit";
        peopleService.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }
}
