package library.utils;

import library.models.Person;
import library.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class PersonValidator implements Validator {

    private final String DATE_PATTERN = "dd.MM.yyyy";
    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
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
        boolean isNameExisting = peopleService.isNameExisting(name);
        if (isNameExisting) errors.rejectValue("name", "", "Name should be unique.");
    }

    private void checkBirthday(Date birthday, Errors errors) {
        boolean dateIsCorrect = true;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = dateFormat.format(birthday);

        try {
            Date parsedDate = dateFormat.parse(formattedDate);
            if (!formattedDate.equals(dateFormat.format(parsedDate))) {
                dateIsCorrect = false;
            }
        } catch (Exception e) {
            dateIsCorrect = false;
        }

        if (!dateIsCorrect) {
            errors.rejectValue("birthday", "", "Неправильный формат даты. Используйте формат dd.MM.yyyy");
        }
    }

}