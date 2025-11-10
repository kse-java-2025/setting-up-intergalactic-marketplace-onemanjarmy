package com.odanylchuk.cosmocatsmarketplace.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CosmicWordCheckValidatorTest {

    private final CosmicWordCheckValidator validator = new CosmicWordCheckValidator();

    @Test
    void isValid_nullAndBlank() {
        assertTrue(validator.isValid(null, null));
        assertTrue(validator.isValid("", null));
        assertTrue(validator.isValid("   ", null));
    }

    @Test
    void isValid_containsCosmicWord() {
        assertTrue(validator.isValid("Amazing space toy", null));
        assertTrue(validator.isValid("Galaxy collar", null));
        assertTrue(validator.isValid("Interstellar gadget", null));
    }

    @Test
    void isValid_withoutCosmicWord() {
        assertFalse(validator.isValid("Regular toy", null));
        assertFalse(validator.isValid("Nice product", null));
    }
}

