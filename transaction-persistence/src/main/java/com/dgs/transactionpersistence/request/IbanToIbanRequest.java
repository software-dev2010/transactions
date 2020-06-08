package com.dgs.transactionpersistence.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IbanToIbanRequest {

    private String type;
    private String fromIban;
    private String toIban;
    private String amount;
    private String description;
}
