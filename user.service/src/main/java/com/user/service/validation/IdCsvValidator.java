package com.user.service.validation;

import com.user.service.validation.annotation.IdCsvValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class IdCsvValidator implements ConstraintValidator<IdCsvValidation, String> {

    @Override
    public void initialize(IdCsvValidation constraintAnnotation) {
    }

    @Override
    public boolean isValid(String idCsv, ConstraintValidatorContext context) {
        if (idCsv == null || idCsv.trim().isEmpty()) {
            return true;
        }

        if (idCsv.length() > 200) {
            context.buildConstraintViolationWithTemplate("CSV string is too long: received " + idCsv.length() + " characters, maximum allowed is 200").addConstraintViolation();
            return false;
        }

        String[] ids = idCsv.split(",");
        if (ids.length == 0) {
            return false;
        }

        return Arrays.stream(ids).allMatch(id -> isAPositiveNumber(id, context));
    }

    private boolean isAPositiveNumber(String id, ConstraintValidatorContext context) {
        try {
            int intId = Integer.parseInt(id.trim());
            return intId > 0;
        } catch (NumberFormatException e) {
            context.buildConstraintViolationWithTemplate("Invalid ID format: '" + id + "'. Only positive integers are allowed").addConstraintViolation();
            return false;
        }
    }
}