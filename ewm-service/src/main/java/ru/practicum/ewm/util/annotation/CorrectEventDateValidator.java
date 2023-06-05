package ru.practicum.ewm.util.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class CorrectEventDateValidator implements ConstraintValidator<CorrectEventDate, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime eventDate, ConstraintValidatorContext constraintValidatorContext) {
        if (eventDate == null) {
            return true;
        }
        return eventDate.isAfter(LocalDateTime.now().plusHours(2));
    }
}
