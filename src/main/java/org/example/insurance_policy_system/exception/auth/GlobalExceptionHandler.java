package org.example.insurance_policy_system.exception.auth;
import org.example.authservice.util.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class) ResponseEntity<ApiResponse<Void>> status(ResponseStatusException e){
        return ResponseEntity.status(e.getStatusCode()).body(ApiResponse.error(e.getReason()==null?"Request failed":e.getReason()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<ApiResponse<Void>> validation(MethodArgumentNotValidException e){
        return ResponseEntity.unprocessableEntity().body(ApiResponse.error(e.getBindingResult().getFieldErrors().stream().map(x->x.getField()+": "+x.getDefaultMessage()).findFirst().orElse("Validation failed")));
    }
    @ExceptionHandler({DataIntegrityViolationException.class}) ResponseEntity<ApiResponse<Void>> conflict(Exception e){
        return ResponseEntity.status(409).body(ApiResponse.error("Duplicate or related record constraint violation"));
    }
    @ExceptionHandler({HttpMessageNotReadableException.class,IllegalArgumentException.class}) ResponseEntity<ApiResponse<Void>> bad(Exception e){
        return ResponseEntity.badRequest().body(ApiResponse.error("Invalid request: "+e.getMessage()));
    }
    @ExceptionHandler(Exception.class) ResponseEntity<ApiResponse<Void>> other(Exception e){
        return ResponseEntity.internalServerError().body(ApiResponse.error("Internal server error"));
    }
}
