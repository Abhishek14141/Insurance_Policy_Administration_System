package org.example.insurance_policy_system.exception.insuranceManagement;


import org.example.insurance_policy_system.dto.insuranceManagement.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
 @ExceptionHandler(ResourceNotFoundException.class)
 public ResponseEntity<ApiResponse<Void>> notFound(ResourceNotFoundException ex) {
  return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.failure(ex.getMessage()));
 }
 @ExceptionHandler({BadRequestException.class, IllegalArgumentException.class})
 public ResponseEntity<ApiResponse<Void>> badRequest(RuntimeException ex) {
  return ResponseEntity.badRequest().body(ApiResponse.failure(ex.getMessage()));
 }
 @ExceptionHandler(MethodArgumentNotValidException.class)
 public ResponseEntity<ApiResponse<String>> validation(MethodArgumentNotValidException ex) {
  String message=ex.getBindingResult().getFieldErrors().stream()
   .map(e->e.getField()+": "+e.getDefaultMessage()).collect(Collectors.joining("; "));
  return ResponseEntity.unprocessableEntity().body(ApiResponse.failure(message));
 }
 @ExceptionHandler(DataIntegrityViolationException.class)
 public ResponseEntity<ApiResponse<Void>> conflict(DataIntegrityViolationException ex) {
  return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.failure("Duplicate value or record is referenced by another record."));
 }
 @ExceptionHandler(Exception.class)
 public ResponseEntity<ApiResponse<Void>> unexpected(Exception ex) {
  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.failure("Unexpected server error."));
 }
}
