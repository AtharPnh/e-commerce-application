package com.athar.ecommerce.customer.handler;

import com.athar.ecommerce.exception.CustomerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleCustomerNotFoundException_shouldReturnNotFoundStatus() {
        // Arrange
        String errorMessage = "Customer with id 123 not found";
        CustomerNotFoundException exception = new CustomerNotFoundException(errorMessage);

        // Act
        ResponseEntity<String> response = exceptionHandler.handler(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }

    @Test
    void handleCustomerNotFoundException_withDifferentMessage_shouldReturnCorrectMessage() {
        // Arrange
        String errorMessage = "Cannot update customer:: Customer with id 999 not found";
        CustomerNotFoundException exception = new CustomerNotFoundException(errorMessage);

        // Act
        ResponseEntity<String> response = exceptionHandler.handler(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }

    @Test
    void handleMethodArgumentNotValidException_shouldReturnBadRequestStatus() {
        // Arrange
        MethodArgumentNotValidException exception = createMethodArgumentNotValidException();

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handler(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().errors());
    }

    @Test
    void handleMethodArgumentNotValidException_shouldReturnValidationErrors() {
        // Arrange
        MethodArgumentNotValidException exception = createMethodArgumentNotValidException();

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handler(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertNotNull(errorResponse.errors());
        assertEquals(2, errorResponse.errors().size());
        assertEquals("Email is required", errorResponse.errors().get("email"));
        assertEquals("First name cannot be empty", errorResponse.errors().get("firstName"));
    }

    @Test
    void handleMethodArgumentNotValidException_withSingleError_shouldReturnSingleError() {
        // Arrange
        MethodArgumentNotValidException exception = createSingleErrorMethodArgumentNotValidException();

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handler(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.errors().size());
        assertEquals("Email format is invalid", errorResponse.errors().get("email"));
    }

    @Test
    void handleMethodArgumentNotValidException_withEmptyErrors_shouldReturnEmptyErrorMap() {
        // Arrange
        MethodArgumentNotValidException exception = createEmptyErrorMethodArgumentNotValidException();

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handler(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertTrue(errorResponse.errors().isEmpty());
    }

    // Helper methods to create test data
    private MethodArgumentNotValidException createMethodArgumentNotValidException() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError emailError = new FieldError("customerRequest", "email", "Email is required");
        FieldError firstNameError = new FieldError("customerRequest", "firstName", "First name cannot be empty");
        
        when(bindingResult.getAllErrors()).thenReturn(List.of(emailError, firstNameError));
        
        return new MethodArgumentNotValidException(null, bindingResult);
    }

    private MethodArgumentNotValidException createSingleErrorMethodArgumentNotValidException() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError emailError = new FieldError("customerRequest", "email", "Email format is invalid");
        
        when(bindingResult.getAllErrors()).thenReturn(List.of(emailError));
        
        return new MethodArgumentNotValidException(null, bindingResult);
    }

    private MethodArgumentNotValidException createEmptyErrorMethodArgumentNotValidException() {
        BindingResult bindingResult = mock(BindingResult.class);
        
        when(bindingResult.getAllErrors()).thenReturn(List.of());
        
        return new MethodArgumentNotValidException(null, bindingResult);
    }
}
