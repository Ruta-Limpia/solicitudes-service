package com.duoc.rutalimpia.solicitudes.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> noEncontrado(RecursoNoEncontradoException e, HttpServletRequest request) {
        return construir(HttpStatus.NOT_FOUND, "NOT_FOUND", e.getMessage(), request);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> rutaNoExiste(NoResourceFoundException e, HttpServletRequest request) {
        return construir(HttpStatus.NOT_FOUND, "NOT_FOUND", "La ruta no existe", request);
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorResponse> reglaNegocio(ReglaNegocioException e, HttpServletRequest request) {
        return construir(HttpStatus.CONFLICT, "CONFLICT", e.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validacion(MethodArgumentNotValidException e, HttpServletRequest request) {
        String mensaje = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .orElse("Datos inválidos");
        return construir(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", mensaje, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> jsonInvalido(HttpMessageNotReadableException e, HttpServletRequest request) {
        return construir(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "El cuerpo de la petición no es válido", request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> noAutenticado(AuthenticationException e, HttpServletRequest request) {
        return construir(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Token ausente o inválido", request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> accesoDenegado(AccessDeniedException e, HttpServletRequest request) {
        return construir(HttpStatus.FORBIDDEN, "FORBIDDEN", "No tienes permiso para este recurso", request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> general(Exception e, HttpServletRequest request) {
        log.error("Error no controlado", e);
        return construir(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Error interno del servidor", request);
    }

    private ResponseEntity<ErrorResponse> construir(HttpStatus status, String error, String mensaje, HttpServletRequest request) {
        ErrorResponse body = new ErrorResponse(LocalDateTime.now(), status.value(), error, mensaje, request.getRequestURI());
        return ResponseEntity.status(status).body(body);
    }
}
