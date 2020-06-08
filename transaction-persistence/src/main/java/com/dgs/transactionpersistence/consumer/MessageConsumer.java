package com.dgs.transactionpersistence.consumer;

import com.dgs.transactionpersistence.request.*;
import com.dgs.transactionpersistence.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static com.dgs.transactionpersistence.util.TransactionsConstants.*;

@Component
@EnableJms
@AllArgsConstructor
public class MessageConsumer {

    private final ObjectMapper objectMapper;
    private final TransactionService transactionService;

    @JmsListener(destination = TRANSACTIONS_QUEUE)
    public void transactionListener(String jsonStr) throws JsonProcessingException {
        String type = jsonStr.substring(9, 10);
        if (type.equals(IBAN_TO_IBAN_TYPE)) {
            IbanToIbanRequest ibanToIbanRequest = objectMapper
                    .readValue(jsonStr, IbanToIbanRequest.class);

            transactionService.sendMoneyIbanToIban(ibanToIbanRequest);
        } else if (type.equals(IBAN_TO_WALLET_TYPE)) {
            IbanToWalletRequest ibanToWalletRequest = objectMapper
                    .readValue(jsonStr, IbanToWalletRequest.class);

            transactionService.sendMoneyIbanToWallet(ibanToWalletRequest);
        } else if (type.equals(WALLET_TO_IBAN_TYPE)) {
            WalletToIbanRequest walletToIbanRequest = objectMapper
                    .readValue(jsonStr, WalletToIbanRequest.class);

            transactionService.sendMoneyWalletToIban(walletToIbanRequest);
        } else if (type.equals(WALLET_TO_WALLET_TYPE)) {
            WalletToWalletRequest walletToWalletRequest = objectMapper
                    .readValue(jsonStr, WalletToWalletRequest.class);

            transactionService.sendMoneyWalletToWallet(walletToWalletRequest);
        }
    }
}