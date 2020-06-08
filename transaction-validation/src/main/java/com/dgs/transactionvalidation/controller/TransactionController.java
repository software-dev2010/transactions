package com.dgs.transactionvalidation.controller;

import com.dgs.transactionvalidation.request.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Queue;
import javax.validation.Valid;

import static com.dgs.transactionvalidation.util.TransactionsConstants.*;

@RestController
@AllArgsConstructor
public class TransactionController {

    private final Queue queue;
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @PostMapping("/ibantoiban")
    public ResponseEntity sendMoneyIbanToIban(
            @Valid @RequestBody IbanToIbanRequest ibanToIbanRequest)
            throws JsonProcessingException {

        ibanToIbanRequest.setType(IBAN_TO_IBAN_TYPE);
        String jsonStr = objectMapper.writeValueAsString(ibanToIbanRequest);

        return sendMessageToProducer(jsonStr);
    }

    @PostMapping("/ibantowallet")
    public ResponseEntity sendMoneyIbanToWallet(
            @Valid @RequestBody IbanToWalletRequest ibanToWalletRequest)
            throws JsonProcessingException {

        ibanToWalletRequest.setType(IBAN_TO_WALLET_TYPE);
        String jsonStr = objectMapper.writeValueAsString(ibanToWalletRequest);

        return sendMessageToProducer(jsonStr);
    }

    @PostMapping("/wallettoiban")
    public ResponseEntity sendMoneyWalletToIban(
            @Valid @RequestBody WalletToIbanRequest walletToIbanRequest)
            throws JsonProcessingException {

        walletToIbanRequest.setType(WALLET_TO_IBAN_TYPE);
        String jsonStr = objectMapper.writeValueAsString(walletToIbanRequest);

        return sendMessageToProducer(jsonStr);
    }

    @PostMapping("/wallettowallet")
    public ResponseEntity sendMoneyWalletToWallet(
            @Valid @RequestBody WalletToWalletRequest walletToWalletRequest)
            throws JsonProcessingException {

        walletToWalletRequest.setType(WALLET_TO_WALLET_TYPE);
        String jsonStr = objectMapper.writeValueAsString(walletToWalletRequest);

        return sendMessageToProducer(jsonStr);
    }

//    @HystrixCommand(fallbackMethod="sendMessageToProducerFail")
    private ResponseEntity sendMessageToProducer(String jsonStr) {
        jmsTemplate.convertAndSend(queue, jsonStr);
        return new ResponseEntity("Transaction successfully completed!", HttpStatus.CREATED);
    }

//    private ResponseEntity sendMessageToProducerFail(String jsonStr) {
//        ...
//    }
}