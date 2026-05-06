package com.cajaviva.cajaviva.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(ResourceNotFoundException exception) {
        logger.warn("Recurso no encontrado: {}", exception.getMessage());
        return buildResponse(exception.getMessage(), "RESOURCE_NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleConflict(ConflictException exception) {
        logger.warn("Conflicto: {}", exception.getMessage());
        return buildResponse(exception.getMessage(), "CONFLICT", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessValidation(BusinessValidationException exception) {
        logger.warn("Error de validación: {}", exception.getMessage());
        return buildResponse(exception.getMessage(), "BUSINESS_VALIDATION_ERROR", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        logger.warn("Validación de request fallida: {}", exception.getMessage());
        return buildResponse("La validación del cuerpo de la solicitud falló.", "REQUEST_VALIDATION_ERROR", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException exception) {
        String message = exception.getMostSpecificCause().getMessage();
        logger.error("Violación de integridad de datos: {}", message);

        if (message.contains("Duplicate entry")) {
            return buildResponse("El recurso ya existe (duplicado).", "DUPLICATE_RESOURCE", HttpStatus.CONFLICT);
        }
        if (message.contains("foreign key constraint")) {
            return buildResponse("El recurso referenciado no existe.", "REFERENCED_RESOURCE_NOT_FOUND", HttpStatus.BAD_REQUEST);
        }
        return buildResponse("Violación de integridad de datos.", "DATA_INTEGRITY_ERROR", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException exception) {
        logger.warn("Validación de path variable fallida: {}", exception.getMessage());
        return buildResponse("El formato del parámetro es inválido.", "INVALID_PARAMETER", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleNotFoundRoutes(Exception exception) {
        logger.warn("Ruta no encontrada: {}", exception.getMessage());
        return buildResponse("La ruta solicitada no existe.", "RESOURCE_NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedException(Exception exception) {
        logger.error("Error inesperado: {} - {}", exception.getClass().getSimpleName(), exception.getMessage(), exception);
        return buildResponse("Ocurrió un error interno inesperado.", "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(String message, String code, HttpStatus status) {
        ApiErrorResponse response = new ApiErrorResponse(message, code, LocalDateTime.now());
        return ResponseEntity.status(status).body(response);
    }
}
