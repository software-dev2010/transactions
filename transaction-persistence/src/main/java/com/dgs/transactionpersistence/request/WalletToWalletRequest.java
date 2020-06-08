package com.dgs.transactionpersistence.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletToWalletRequest {

    private String type;
    private String fromWalletNumber;
    private String toWalletNumber;
    private String amount;
    private String description;
}
