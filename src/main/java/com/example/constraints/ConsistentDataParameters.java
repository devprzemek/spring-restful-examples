package com.example.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = ConsistentDataParametersValidator.class)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RUNTIME)
@Documented
public @interface ConsistentDataParameters {
    String message() default "End date must be after begin date and both must be in the future";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
