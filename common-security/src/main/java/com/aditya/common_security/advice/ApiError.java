package com.aditya.common_security.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ApiError {
    private String message;
    private HttpStatus httpStatus;
    private List<String> subErrors;

}
