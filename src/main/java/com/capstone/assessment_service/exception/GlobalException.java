package com.capstone.assessment_service.exception;

import com.capstone.assessment_service.dto.ClientResponseFormValidationErrorDto;
import com.capstone.assessment_service.dto.ClientResponseFormatDto;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@RestControllerAdvice
@Hidden
public class GlobalException {
    static final String FORM_TITLE_MESSAGE = "Form validation Error";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ClientResponseFormatDto> handleException
            (Exception exception) {

        ClientResponseFormatDto response = ClientResponseFormatDto.builder()
                .success(false)
                .message(exception.getMessage())
                .errors(null)
                .data(null)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Error.class)
    public ResponseEntity<ClientResponseFormatDto> handleError
            (Exception exception) {

        ClientResponseFormatDto response = ClientResponseFormatDto.builder()
                .success(false)
                .message(exception.getMessage())
                .errors(null)
                .data(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ClientResponseFormatDto> handle404(NoHandlerFoundException exception) {

        ClientResponseFormatDto response = ClientResponseFormatDto.builder()
                .success(false)
                .message(exception.getMessage())
                .errors(null)
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ClientResponseFormatDto> handleUnauthorized(AccessDeniedException exception) {
        ClientResponseFormatDto response = ClientResponseFormatDto.builder()
                .success(false)
                .message(exception.getMessage())
                .errors(null)
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(response);
    }

    // Handle JSON deserialization issues (@RequestBody)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ClientResponseFormValidationErrorDto> handleJacksonErrors(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        String message = "Invalid input format";
        String fieldName = null;

        if (cause instanceof JsonMappingException mappingEx) {
            // Field name from the first path element if available
            if (!mappingEx.getPath().isEmpty()) {
                fieldName = mappingEx.getPath().get(0).getFieldName();
            }

            Class<?> targetType = null;
            if (mappingEx instanceof InvalidFormatException invalidFormat) {
                targetType = invalidFormat.getTargetType();
            }

            if (targetType != null && targetType.isEnum()) {
                // Enum validation
                message = String.format("Invalid value for '%s'", fieldName);
            } else if (UUID.class.equals(targetType)) {
                message = String.format("'%s' provided doesn't exist", fieldName);
                ClientResponseFormValidationErrorDto response = ClientResponseFormValidationErrorDto.builder()
                        .success(false)
                        .message(message)
                        .errors(null)
                        .data(null)
                        .build();

                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                // unknown target type
                message = String.format("Field '%s' has invalid value(s)", fieldName);
            }
        }

        ClientResponseFormValidationErrorDto response = ClientResponseFormValidationErrorDto.builder()
                .success(false)
                .message(FORM_TITLE_MESSAGE)
                .errors(List.of(message))
                .data(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    // Handle validation failures on @ModelAttribute and @RequestBody
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<ClientResponseFormValidationErrorDto> handleValidationExceptions(Exception ex) {
        BindingResult bindingResult = (ex instanceof MethodArgumentNotValidException methodEx)
                ? methodEx.getBindingResult()
                : ((BindException) ex).getBindingResult();

        List<String> errors = bindingResult.getFieldErrors().stream()
                .map(error -> {
                    // if it is a type mismatch override message
                    if ("typeMismatch".equals(error.getCode())) {
                        return String.format("Field '%s' has invalid value(s)", error.getField());
                    }
                    // otherwise use @Valid annotation message
                    return String.format("%s: %s", error.getField(), error.getDefaultMessage());
                })
                .toList();

        ClientResponseFormValidationErrorDto response = ClientResponseFormValidationErrorDto.builder()
                .success(false)
                .message(FORM_TITLE_MESSAGE)
                .errors(errors)
                .data(null)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    // Handle wrong enum/UUID values
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ClientResponseFormValidationErrorDto> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String fieldName = ex.getName();
        String message;

        if (ex.getRequiredType().isEnum()) {
            message = String.format("Invalid value for '%s'.", fieldName);

        } else if (UUID.class.equals(ex.getRequiredType())) {
            message = String.format("'%s' provided doesn't exist", fieldName);
            ClientResponseFormValidationErrorDto response = ClientResponseFormValidationErrorDto.builder()
                    .success(false)
                    .message(message)
                    .errors(null)
                    .data(null)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } else {
            message = String.format("Invalid value for field '%s'", fieldName);
        }

        ClientResponseFormValidationErrorDto response = ClientResponseFormValidationErrorDto.builder()
                .success(false)
                .message(FORM_TITLE_MESSAGE)
                .errors(List.of(message))
                .data(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(CapsuleNotFoundException.class)
    public ResponseEntity<ClientResponseFormatDto> handleCapsuleNotFound(
            CapsuleNotFoundException exception) {
        ClientResponseFormatDto response = ClientResponseFormatDto.builder()
                .success(false)
                .message(exception.getMessage())
                .errors(null)
                .data(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CapsuleExistsException.class)
    public ResponseEntity<ClientResponseFormatDto> handleCapsuleExists
            (CapsuleExistsException exception) {

        ClientResponseFormatDto response = ClientResponseFormatDto.builder()
                .success(false)
                .message(exception.getMessage())
                .errors(null)
                .data(null)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

}
