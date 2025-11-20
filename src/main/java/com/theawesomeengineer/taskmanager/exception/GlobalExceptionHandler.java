package com.theawesomeengineer.taskmanager.exception;

import com.theawesomeengineer.taskmanager.model.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

/**
 * Global Exception Handler - catches errors from the whole application
 * @RestControllerAdvice tells Spring to use this for handling exceptions
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle "task not found" errors - returns 404
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Error> handleTaskNotFound(TaskNotFoundException exception) {
        // Create error response
        Error error = new Error();
        error.setMessage(exception.getMessage());
        error.setTimestamp(OffsetDateTime.now(ZoneOffset.UTC));
        error.setDetails("The requested task does not exist in the database");

        // Return 404 NOT FOUND
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Handle validation errors (like missing required fields) - returns 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> handleValidationError(MethodArgumentNotValidException exception) {
        // Get all validation errors
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        // Collect error messages
        List<String> errorMessages = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            errorMessages.add(fieldError.getDefaultMessage());
        }

        // Combine all error messages
        String allErrors = String.join(", ", errorMessages);

        // Create error response
        Error error = new Error();
        error.setMessage("Validation failed");
        error.setTimestamp(OffsetDateTime.now(ZoneOffset.UTC));
        error.setDetails(allErrors);

        // Return 400 BAD REQUEST
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Handle all other unexpected errors - returns 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleGenericError(Exception exception) {
        // Create error response
        Error error = new Error();
        error.setMessage("Internal server error");
        error.setTimestamp(OffsetDateTime.now(ZoneOffset.UTC));
        error.setDetails(exception.getMessage());

        // Return 500 INTERNAL SERVER ERROR
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
