package Unillanos.AsistenciaMonitor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.function.Supplier;


public abstract class BaseController {

    protected <T> ResponseEntity<T> createSuccessResponse(T data) {
        return ResponseEntity.ok(data);
    }

    protected ResponseEntity<?> createErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(message);
    }
    protected <T> ResponseEntity<?> handleRequest(Supplier<T> action) {
        try {
            return createSuccessResponse(action.get());
        } catch (RuntimeException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
        }
    }
}