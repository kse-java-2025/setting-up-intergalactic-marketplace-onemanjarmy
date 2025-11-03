package com.odanylchuk.cosmocatsmarketplace.exception;

import lombok.Getter;

@Getter
public class ResourceMovedPermanentlyException extends RuntimeException {
    private final String newSlug;

    public ResourceMovedPermanentlyException(String newSlug) {
        super("Resource has moved to: " + newSlug);
        this.newSlug = newSlug;
    }
}