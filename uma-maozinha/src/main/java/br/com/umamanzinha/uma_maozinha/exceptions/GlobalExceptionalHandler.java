package br.com.umamanzinha.uma_maozinha.exceptions;

import br.com.umamanzinha.uma_maozinha.dtos.ExceptionDTO;
import br.com.umamanzinha.uma_maozinha.dtos.FieldErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionalHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handleNotFound (ResourceNotFoundException ex, HttpServletRequest request){
        ExceptionDTO exception = new ExceptionDTO(
                HttpStatus.NOT_FOUND.value(),
                "Resource not found!",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDTO> handleValidationError(MethodArgumentNotValidException ex,
                                                              HttpServletRequest request) {
        List<FieldErrorDTO> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new FieldErrorDTO(err.getField(), err.getDefaultMessage()))
                .toList();

        ExceptionDTO dto = new ExceptionDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now(),
                fieldErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDTO> handleConstraintViolation(ConstraintViolationException ex,
                                                                  HttpServletRequest request) {
        List<FieldErrorDTO> fieldErrors = ex.getConstraintViolations().stream()
                .map(v -> new FieldErrorDTO(
                        v.getPropertyPath().toString(),
                        v.getMessage()))
                .toList();

        ExceptionDTO dto = new ExceptionDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now(),
                fieldErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }
}

