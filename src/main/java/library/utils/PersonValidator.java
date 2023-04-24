package library.utils;

import library.dao.PersonDAO;
import library.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class PersonValidator implements Validator {

    private final String DATE_PATTERN = "dd.MM.yyyy";
    private final PersonDAO personDAO;

    @Autowired
    public PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

        // Unique name - only when creating
        if (person.getId() == 0) checkName(person.getName(), errors);

        // Birthday pattern
        checkBirthday(person.getBirthday(), errors);
    }

    private void checkName(String name, Errors errors) {
        boolean isNameExisting = personDAO.isNameExisting(name);
        if (isNameExisting) errors.rejectValue("name", "", "Name should be unique.");
    }

    private void checkBirthday(String birthday, Errors errors) {
        boolean dateIsCorrect = true;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        try {
            LocalDate.parse(birthday, formatter);
        } catch (Exception e) {
            dateIsCorrect = false;
        }
        if (!dateIsCorrect) errors.rejectValue("birthday", "", "Date format is \"" + DATE_PATTERN + "\"");
    }
}