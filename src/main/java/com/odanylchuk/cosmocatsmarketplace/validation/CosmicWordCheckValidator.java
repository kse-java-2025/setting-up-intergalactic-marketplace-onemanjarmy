package com.odanylchuk.cosmocatsmarketplace.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class CosmicWordCheckValidator implements ConstraintValidator<CosmicWordCheck, String> {

    private static final List<String> COSMIC_WORDS = Arrays.asList(
            "star", "galaxy", "comet", "space", "cosmic", 
            "nebula", "meteor", "asteroid", "planet", "lunar",
            "solar", "orbit", "satellite", "universe", "astral",
            "celestial", "interstellar", "supernova", "quasar", "pulsar"
    );

    @Override
    public void initialize(CosmicWordCheck constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        String lowerCaseValue = value.toLowerCase();
        return COSMIC_WORDS.stream().anyMatch(lowerCaseValue::contains);
    }
}
