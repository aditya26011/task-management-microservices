package com.aditya.tutorial.advice;


import com.aditya.tutorial.exceptions.InvalidRequestException;
import com.aditya.tutorial.exceptions.ResourceNotFoundException;
import com.aditya.tutorial.exceptions.UserAlreadyExistsException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException exception){
        ApiError apiError=new ApiError();
        apiError.setMessage(exception.getMessage());
        apiError.setHttpStatus(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(apiError,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationError(MethodArgumentNotValidException exception){
      List<String>errorList= exception
                .getBindingResult()
                .getAllErrors()
                .stream().
                map((error)->error.getDefaultMessage())
                .collect(Collectors.toList());

      ApiError apiError=new ApiError();
      apiError.setMessage(exception.getMessage());
      apiError.setSubErrors(errorList);
      apiError.setHttpStatus(HttpStatus.BAD_REQUEST);

      return new ResponseEntity<>(apiError,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleUserAlreadyExistsException(UserAlreadyExistsException exception){
        ApiError apiError=new ApiError();
        apiError.setHttpStatus(HttpStatus.CONFLICT);
        apiError.setMessage(exception.getMessage());

        return new ResponseEntity<>(apiError,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException exception){
        ApiError apiError=new ApiError();
        apiError.setMessage(exception.getLocalizedMessage());
        apiError.setHttpStatus(HttpStatus.UNAUTHORIZED);

        return  new ResponseEntity<>(apiError,HttpStatus.UNAUTHORIZED);

    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> handleJwtException(JwtException exception){
        ApiError apiError=new ApiError();
        apiError.setMessage(exception.getLocalizedMessage());
        apiError.setHttpStatus(HttpStatus.UNAUTHORIZED);

        return  new ResponseEntity<>(apiError,HttpStatus.UNAUTHORIZED);

    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiError> handelInvalidRequestException(InvalidRequestException ex){
        ApiError apiError=new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setHttpStatus(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(apiError,HttpStatus.BAD_REQUEST);
    }
}
