package com.example.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import java.time.Duration;
import java.time.LocalDateTime;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class ConsistentDataParametersValidator implements ConstraintValidator<ConsistentDataParameters, Object[]> {
    @Override
    public boolean isValid(Object[] object, ConstraintValidatorContext context) {

        if(object == null){
            return true;
        }

        if(object[0] == null || object[1] == null){
            return true;
        }

        if(!(object[0] instanceof LocalDateTime) || !(object[1] instanceof LocalDateTime)){
            throw new IllegalArgumentException("Illegal method signature, expected two parameters of type LocalDateTime.");
        }


        return !Duration.between((LocalDateTime) object[0], (LocalDateTime) object[1]).isNegative();

    }
}
