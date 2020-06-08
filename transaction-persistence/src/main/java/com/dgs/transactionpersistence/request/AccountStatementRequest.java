package com.dgs.transactionpersistence.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class AccountStatementRequest {

    @NotBlank(message = "Please provide a valid CNP")
    @Pattern(regexp = "^[12](0[1-9]|[1-9][0-9])(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])([0-9]{6})$",
            message = "CNP validation failed")
    private String cnp;
}
