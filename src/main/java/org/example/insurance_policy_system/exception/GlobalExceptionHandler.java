package org.example.insurance_policy_system.exception;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.example.insurance_policy_system.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message, HttpServletRequest request) {

        ErrorResponse response = new ErrorResponse(false, status.value(), status.getReasonPhrase(), message,
                request.getRequestURI(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(response);
    }

    // 404 - Case not found
    @ExceptionHandler(CaseIdNotFound.class)
    public ResponseEntity<ErrorResponse> handleCaseNotFound(
            CaseIdNotFound ex, HttpServletRequest request) {
        log.warn("Case not found: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    // 400 - Business validation error
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {

        log.warn("Business error: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    // 400 - Expired quote
    @ExceptionHandler(QuoteExpiredException.class)
    public ResponseEntity<ErrorResponse> handleExpiredQuote(
            QuoteExpiredException ex, HttpServletRequest request) {

        log.warn("Quote expired: {}", ex.getMessage());

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    // 409 - Duplicate case
    @ExceptionHandler(DuplicateCaseException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateCase(
            DuplicateCaseException ex, HttpServletRequest request) {

        log.warn("Duplicate case: {}", ex.getMessage());

        return buildErrorResponse(
                HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    // 400 - Quote status invalid
    @ExceptionHandler(InvalidQuoteStatusException.class)
    public ResponseEntity<ErrorResponse> handleInvalidQuoteStatusException(
            InvalidQuoteStatusException ex, HttpServletRequest request) {

        log.warn("Invalid quote status: {}", ex.getMessage());

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    // 400 - DTO validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": "
                        + error.getDefaultMessage())
                .findFirst()
                .orElse("Request validation failed");

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST, message, request);
    }

    // 400 - Invalid JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidBody(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid request body or JSON format",
                request);
    }

    // 400 - Invalid parameter type
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid value for parameter: " + ex.getName(),
                request);
    }

    // 400 - Missing request parameter
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameter(
            MissingServletRequestParameterException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Missing parameter: " + ex.getParameterName(),
                request);
    }

    // 400 - Constraint validation
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    // 502/503 - Feign client errors
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(
            FeignException ex, HttpServletRequest request) {

        log.error("Microservice call failed", ex);

        HttpStatus status = ex.status() == 503
                ? HttpStatus.SERVICE_UNAVAILABLE
                : HttpStatus.BAD_GATEWAY;

        String message = status == HttpStatus.SERVICE_UNAVAILABLE
                ? "Required microservice is unavailable"
                : "Failed to communicate with required microservice";

        return buildErrorResponse(status, message, request);
    }

    // 400 - Illegal argument
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    // 409 - Database constraint violation (for example, duplicate product code/category name).
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(
            DataIntegrityViolationException ex, HttpServletRequest request) {
        log.warn("Database constraint violation at {}", request.getRequestURI(), ex);
        return buildErrorResponse(HttpStatus.CONFLICT,
                "The request conflicts with existing data or a database constraint", request);
    }

    // 409 - Concurrent update conflict.
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLock(
            ObjectOptimisticLockingFailureException ex, HttpServletRequest request) {
        log.warn("Concurrent update conflict at {}", request.getRequestURI(), ex);
        return buildErrorResponse(HttpStatus.CONFLICT,
                "The record was changed by another request. Reload it and try again", request);
    }

    // 500 - Unexpected server error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(
            Exception ex, HttpServletRequest request) {

        log.error("Unexpected error at {}", request.getRequestURI(), ex);

        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected server error occurred", request);
    }
}
