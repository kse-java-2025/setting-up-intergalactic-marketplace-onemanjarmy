package com.odanylchuk.cosmocatsmarketplace.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleResourceNotFoundException_returnsProblemJson() {
        HttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/products/nonexist");
        ResourceNotFoundException ex = new ResourceNotFoundException("Product", "slug", "nonexist");

        ResponseEntity<ErrorResponse> resp = handler.handleResourceNotFoundException(ex, request);
        assertEquals(404, resp.getStatusCode().value());
        assertEquals("application/problem+json", Objects.requireNonNull(resp.getHeaders().getContentType()).toString());
        assertNotNull(resp.getBody());
        assertTrue(resp.getBody().getDetail().contains("not found"));
    }

    @Test
    void handleMethodArgumentNotValidException_returnsValidationError() {
        HttpServletRequest request = new MockHttpServletRequest("POST", "/api/v1/products");

        Object target = new Object();
        BindingResult bindingResult = new BeanPropertyBindingResult(target, "obj");
        bindingResult.addError(new FieldError("obj", "name", "must contain cosmic word"));

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<ValidationError> resp = handler.handleMethodArgumentNotValidException(ex, request);
        assertEquals(400, resp.getStatusCode().value());
        assertEquals("application/problem+json", Objects.requireNonNull(resp.getHeaders().getContentType()).toString());
        assertNotNull(resp.getBody());
        assertEquals(1, resp.getBody().getErrors().size());
        assertEquals("name", resp.getBody().getErrors().getFirst().getFieldName());
    }

    @Test
    void handleResourceMovedPermanentlyException_setsLocationHeader() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/products/old-slug");
        ResourceMovedPermanentlyException ex = new ResourceMovedPermanentlyException("new-slug");

        ResponseEntity<Void> resp = handler.handleResourceMovedPermanentlyException(ex, request);
        assertEquals(301, resp.getStatusCode().value());
        assertTrue(Objects.requireNonNull(resp.getHeaders().getLocation()).toString().contains("new-slug"));
    }

    @Test
    void handleConstraintViolationException_returnsValidationError() {
        HttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/products");

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path mockPath = mock(Path.class);
        when(mockPath.toString()).thenReturn("param.name");
        when(violation.getPropertyPath()).thenReturn(mockPath);
        when(violation.getMessage()).thenReturn("must be cosmic");

        ConstraintViolationException ex = new ConstraintViolationException(Set.of(violation));

        ResponseEntity<ValidationError> resp = handler.handleConstraintViolationException(ex, request);
        assertEquals(400, resp.getStatusCode().value());
        assertEquals("application/problem+json", Objects.requireNonNull(resp.getHeaders().getContentType()).toString());
        assertNotNull(resp.getBody());
        assertEquals(1, resp.getBody().getErrors().size());
    }

    @Test
    void handleGlobalException_returnsInternalError() {
        HttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/products");
        Exception ex = new RuntimeException("boom");

        ResponseEntity<ErrorResponse> resp = handler.handleGlobalException(ex, request);
        assertEquals(500, resp.getStatusCode().value());
        assertEquals("application/problem+json", Objects.requireNonNull(resp.getHeaders().getContentType()).toString());
        assertNotNull(resp.getBody());
        assertTrue(resp.getBody().getDetail().contains("boom"));
    }
}
