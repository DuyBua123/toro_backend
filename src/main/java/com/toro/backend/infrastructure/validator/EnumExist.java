package com.toro.backend.infrastructure.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.RECORD_COMPONENT;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ FIELD, PARAMETER, RECORD_COMPONENT })
@Retention(RUNTIME)
@Constraint(validatedBy = EnumExistValidator.class)
public @interface EnumExist {

    Class<? extends Enum<?>> enumClass();

    String message() default "Enum value does not exist.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
