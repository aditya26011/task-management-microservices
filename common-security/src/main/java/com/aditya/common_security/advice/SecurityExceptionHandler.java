package com.aditya.common_security.advice;


import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class SecurityExceptionHandler {



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


}
