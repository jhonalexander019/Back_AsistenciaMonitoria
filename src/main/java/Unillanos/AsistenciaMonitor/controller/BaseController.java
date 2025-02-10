package Unillanos.AsistenciaMonitor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {

    protected <T> ResponseEntity<T> createSuccessResponse(T data) {
        return ResponseEntity.ok(data);
    }

    protected ResponseEntity<?> createErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(message);
    }
}