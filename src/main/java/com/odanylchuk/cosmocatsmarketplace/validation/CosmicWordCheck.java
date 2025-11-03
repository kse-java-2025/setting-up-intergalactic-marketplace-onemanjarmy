package com.odanylchuk.cosmocatsmarketplace.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CosmicWordCheckValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CosmicWordCheck {

    String message() default "Product name must contain cosmic words like 'star', 'galaxy', 'comet', 'space', 'cosmic', 'nebula', 'meteor', 'asteroid', 'planet'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
