package ru.practicum.ewm.util.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CorrectEventDateValidator.class)
public @interface CorrectEventDate {
    String message() default "The date of the event should not be earlier than 2 hours";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
