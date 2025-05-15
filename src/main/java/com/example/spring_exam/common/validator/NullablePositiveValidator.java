package com.example.spring_exam.common.validator;


import com.example.spring_exam.common.validator.annotation.NullablePositive;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NullablePositiveValidator implements ConstraintValidator<NullablePositive, Long> {
    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return value == null || value > 0;
    }
}