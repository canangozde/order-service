
package com.example.handler;

import com.example.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleResourceNotFoundException() {
        ResponseEntity<Object> response = handler.handleEntityNotFound(new ResourceNotFoundException("Resource missing"));
        assertEquals(404, response.getStatusCodeValue());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Resource missing", body.get("message"));
        assertEquals("Not Found", body.get("error"));
    }

    @Test
    void testHandleEntityNotFoundException() {
        ResponseEntity<Object> response = handler.handleEntityNotFound(new EntityNotFoundException("Entity not found"));
        assertEquals(404, response.getStatusCodeValue());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Entity not found", body.get("message"));
        assertEquals("Not Found", body.get("error"));
    }

    @Test
    void testHandleValidationErrors() {
        BeanPropertyBindingResult result = new BeanPropertyBindingResult(new Object(), "objectName");
        result.addError(new FieldError("objectName", "field1", "must not be null"));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, result);

        ResponseEntity<Object> response = handler.handleValidationErrors(ex);
        assertEquals(400, response.getStatusCodeValue());

        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Validation Failed", body.get("error"));
        Map<String, String> details = (Map<String, String>) body.get("details");
        assertEquals("must not be null", details.get("field1"));
    }

    @Test
    void testHandleBadCredentials() {
        ResponseEntity<Map<String, Object>> response = handler.handleBadCredentials(new BadCredentialsException("Bad credentials"));
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Unauthorized", response.getBody().get("error"));
        assertEquals("Invalid credentials", response.getBody().get("message"));
    }

    @Test
    void testHandleAccessDenied() {
        ResponseEntity<Map<String, Object>> response = handler.handleAccessDenied(new AccessDeniedException("Forbidden"));
        assertEquals(403, response.getStatusCodeValue());
        assertEquals("Forbidden", response.getBody().get("error"));
        assertEquals("You do not have permission to access this resource.", response.getBody().get("message"));
    }

    @Test
    void testHandleRuntimeException() {
        ResponseEntity<Object> response = handler.handleRuntimeException(new RuntimeException("Unexpected error"));
        assertEquals(500, response.getStatusCodeValue());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Unexpected error", body.get("message"));
        assertEquals("Internal Server Error", body.get("error"));
    }

    @Test
    void testHandleGeneralException() {
        ResponseEntity<Map<String, Object>> response = handler.handleGeneral(new Exception("General error"));
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Internal Server Error", response.getBody().get("error"));
        assertEquals("General error", response.getBody().get("message"));
    }
}
