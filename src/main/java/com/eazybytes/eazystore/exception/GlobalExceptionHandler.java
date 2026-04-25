package com.eazybytes.eazystore.exception;

import com.eazybytes.eazystore.dto.ErrorResponseDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
public ResponseEntity<ErrorResponseDto> handleGlobalExceptionHandler(Exception exception, WebRequest webRequest){


        ErrorResponseDto errorResponseDto =new ErrorResponseDto(webRequest.getDescription(false), HttpStatus.INTERNAL_SERVER_ERROR,exception.getMessage(), LocalDateTime.now());
         log.error("An Exception Occured Due to : {}", exception.getMessage());
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto);
}



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception){
        Map<String,String> errors=new HashMap<>();
       List<FieldError> fieldErrors= exception.getBindingResult().getFieldErrors();
       fieldErrors.forEach(e->errors.put(e.getField(),e.getDefaultMessage()));
        log.error("An Exception Occured Due to : {}", exception.getMessage());
       return ResponseEntity.badRequest().body(errors);
    }




    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstriantViolationException(ConstraintViolationException exception){
        Map<String,String> errors=new HashMap<>();
      Set<ConstraintViolation<?>> constraintViolations=exception.getConstraintViolations();
      constraintViolations.forEach(e->errors.put(e.getPropertyPath().toString(),e.getMessage()));
        log.error("An Exception Occured Due to : {}", exception.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<ErrorResponseDto> hanadleResourceNotFoundException(ResourceNotFoundException exception,WebRequest webRequest){
        ErrorResponseDto errorResponseDto=new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                LocalDateTime.now()
        );
                return new ResponseEntity<>(errorResponseDto,HttpStatus.NOT_FOUND);

}



    @ExceptionHandler(org.springframework.web.client.HttpClientErrorException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpClientError(
            org.springframework.web.client.HttpClientErrorException ex,
            WebRequest webRequest) {

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.valueOf(ex.getStatusCode().value()),
                ex.getResponseBodyAsString(),
                LocalDateTime.now()
        );

        log.error("HTTP Client Error: {}", ex.getMessage());

        return ResponseEntity.status(ex.getStatusCode()).body(errorResponseDto);
    }





}
