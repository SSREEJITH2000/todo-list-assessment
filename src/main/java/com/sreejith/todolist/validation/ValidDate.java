package com.sreejith.todolist.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = DateValidator.class)
public @interface ValidDate {
    String message() default "Invalid date format or date is in the past";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
