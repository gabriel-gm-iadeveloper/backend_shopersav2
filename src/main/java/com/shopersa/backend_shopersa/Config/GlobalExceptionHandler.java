package com.shopersa.backend_shopersa.Config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  /* ---------- Helpers ---------- */
  private Map<String, Object> baseBody(HttpStatus status, String message, HttpServletRequest req) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", Instant.now().toString());
    body.put("status", status.value());
    body.put("error", status.getReasonPhrase());
    body.put("message", message);
    body.put("path", req.getRequestURI());
    return body;
  }

  /* ---------- 400 Bad Request (validaciones / payload) ---------- */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex,
                                                              HttpServletRequest req) {
    Map<String, String> fields = new LinkedHashMap<>();
    ex.getBindingResult().getFieldErrors()
        .forEach(err -> fields.put(err.getField(), err.getDefaultMessage()));

    Map<String, Object> body = baseBody(HttpStatus.BAD_REQUEST, "Validación fallida", req);
    body.put("fields", fields);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, Object>> handleConstraint(ConstraintViolationException ex,
                                                              HttpServletRequest req) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(baseBody(HttpStatus.BAD_REQUEST, ex.getMessage(), req));
  }

  @ExceptionHandler({
      MethodArgumentTypeMismatchException.class,
      HttpMessageNotReadableException.class,
      MissingServletRequestParameterException.class
  })
  public ResponseEntity<Map<String, Object>> handleBadRequest(Exception ex, HttpServletRequest req) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(baseBody(HttpStatus.BAD_REQUEST, ex.getMessage(), req));
  }

  /* ---------- 403 Forbidden ---------- */
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Map<String, Object>> handleForbidden(AccessDeniedException ex,
                                                             HttpServletRequest req) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(baseBody(HttpStatus.FORBIDDEN, "No autorizado", req));
  }

  /* ---------- 404 Not Found ---------- */
  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<Map<String, Object>> handleNoHandler(NoHandlerFoundException ex,
                                                             HttpServletRequest req) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(baseBody(HttpStatus.NOT_FOUND, "Ruta no encontrada", req));
  }

  /* ---------- 409 Conflict (integridad de datos) ---------- */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Map<String, Object>> handleConflict(DataIntegrityViolationException ex,
                                                            HttpServletRequest req) {
    String detail = ex.getMostSpecificCause() != null
        ? ex.getMostSpecificCause().getMessage()
        : ex.getMessage();
    Map<String, Object> body = baseBody(HttpStatus.CONFLICT, "Conflicto de datos", req);
    body.put("detail", detail);
    return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
  }

  /* ---------- 413 Payload Too Large (uploads) ---------- */
  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<Map<String, Object>> handleTooLarge(MaxUploadSizeExceededException ex,
                                                            HttpServletRequest req) {
    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
        .body(baseBody(HttpStatus.PAYLOAD_TOO_LARGE, "Archivo demasiado grande", req));
  }

  /* ---------- Fallback: 404 o 400 según mensaje ---------- */
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex,
                                                           HttpServletRequest req) {
    String m = ex.getMessage() == null ? "" : ex.getMessage().toLowerCase();
    HttpStatus st = (m.contains("no encontrado") || m.contains("no existe"))
        ? HttpStatus.NOT_FOUND
        : HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(st).body(baseBody(st, ex.getMessage(), req));
  }
}
