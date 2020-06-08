package com.dgs.transactionvalidation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IbanToWalletRequest {

    private String type;

    @NotBlank(message = "Please provide a valid IBAN")
    @Pattern(regexp = "[a-zA-Z]{2}\\d{2}[ ]\\d{4}[ ]\\d{4}[ ]\\d{4}[ ]\\d{4}[ ]\\d{2}|DE\\d{20}",
            message = "IBAN validation failed")
    private String fromIban;

    @NotBlank(message = "Please provide a valid wallet number")
    @Pattern(regexp = "\\d{26}",
            message = "Wallet number validation failed")
    private String toWalletNumber;

    @NotBlank(message = "Please provide an amount")
    private String amount;

    @NotBlank(message = "Please provide a description")
    private String description;
}
