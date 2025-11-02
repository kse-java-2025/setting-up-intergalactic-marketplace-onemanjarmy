package com.odanylchuk.cosmocatsmarketplace.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * RFC 9457 compliant global exception handler.
 * Returns application/problem+json content type for all error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String PROBLEM_JSON_CONTENT_TYPE = "application/problem+json";

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .type("https://cosmocats.galaxy/errors/not-found")
                .title("Resource Not Found")
                .status(HttpStatus.NOT_FOUND.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(PROBLEM_JSON_CONTENT_TYPE));

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .headers(headers)
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<ParamsViolationDetails> violations = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::mapFieldError)
                .collect(Collectors.toList());

        ValidationError validationError = ValidationError.builder()
                .type("https://cosmocats.galaxy/errors/validation")
                .title("Validation Failed")
                .status(HttpStatus.BAD_REQUEST.value())
                .detail("One or more fields have validation errors")
                .instance(request.getRequestURI())
                .errors(violations)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(PROBLEM_JSON_CONTENT_TYPE));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .headers(headers)
                .body(validationError);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationError> handleConstraintViolationException(
            ConstraintViolationException ex, HttpServletRequest request) {

        List<ParamsViolationDetails> violations = ex.getConstraintViolations()
                .stream()
                .map(this::mapConstraintViolation)
                .collect(Collectors.toList());

        ValidationError validationError = ValidationError.builder()
                .type("https://cosmocats.galaxy/errors/validation")
                .title("Constraint Violation")
                .status(HttpStatus.BAD_REQUEST.value())
                .detail("One or more constraints were violated")
                .instance(request.getRequestURI())
                .errors(violations)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(PROBLEM_JSON_CONTENT_TYPE));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .headers(headers)
                .body(validationError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .type("https://cosmocats.galaxy/errors/internal-server-error")
                .title("Internal Server Error")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .detail("An unexpected error occurred: " + ex.getMessage())
                .instance(request.getRequestURI())
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(PROBLEM_JSON_CONTENT_TYPE));

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .headers(headers)
                .body(errorResponse);
    }

    private ParamsViolationDetails mapFieldError(FieldError fieldError) {
        return ParamsViolationDetails.builder()
                .fieldName(fieldError.getField())
                .reason(fieldError.getDefaultMessage())
                .build();
    }

    private ParamsViolationDetails mapConstraintViolation(ConstraintViolation<?> violation) {
        String fieldName = violation.getPropertyPath().toString();
        return ParamsViolationDetails.builder()
                .fieldName(fieldName)
                .reason(violation.getMessage())
                .build();
    }
}
