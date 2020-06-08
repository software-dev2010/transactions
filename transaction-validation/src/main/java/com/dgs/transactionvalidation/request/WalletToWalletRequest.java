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
public class WalletToWalletRequest {

    private String type;

    @NotBlank(message = "Please provide a valid wallet number")
    @Pattern(regexp = "\\d{26}",
            message = "Wallet number validation failed")
    private String fromWalletNumber;

    @NotBlank(message = "Please provide a valid wallet number")
    @Pattern(regexp = "\\d{26}",
            message = "Wallet number validation failed")
    private String toWalletNumber;

    @NotBlank(message = "Please provide an amount")
    private String amount;

    @NotBlank(message = "Please provide a description")
    private String description;
}
