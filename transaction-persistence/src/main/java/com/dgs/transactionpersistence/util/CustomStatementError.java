package com.dgs.transactionpersistence.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@AllArgsConstructor
public class CustomStatementError {

    private HttpStatus status;
    private String message;
    private List<String> errors;
}
