package com.dgs.transactionpersistence.service.impl;

import com.dgs.transactionpersistence.entity.Account;
import com.dgs.transactionpersistence.entity.Transaction;
import com.dgs.transactionpersistence.repository.AccountRepository;
import com.dgs.transactionpersistence.repository.TransactionRepository;
import com.dgs.transactionpersistence.request.IbanToIbanRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class TransactionServiceImplTest {

    private final static String PAYER_IBAN = "SA89 3704 0044 0532 0130 00";
    private final static String PAYEE_IBAN = "GR96 0810 0010 0000 0123 45";
    private final static String IBAN_TO_IBAN_TYPE = "1";
    private final static String AMOUNT = "1000";
    private final static String DESCRIPTION = "Payment from John's iban to Bill's iban";
    private final static Long PAYER_ACCOUNT_ID = 1L;
    private final static String PAYER_ACCOUNT_NUMBER = "10982738574984850120958235";
    private final static String PAYER_NAME = "John Book";
    private final static String PAYER_CNP = "1831222132132";
    private final static BigDecimal PAYER_CURRENT_BALANCE = new BigDecimal("13766");
    private final static Long PAYEE_ACCOUNT_ID = 2L;
    private final static String PAYEE_ACCOUNT_NUMBER = "41230192109218940921875412";
    private final static String PAYEE_NAME = "Bill Black";
    private final static String PAYEE_CNP = "1880724190281";
    private final static BigDecimal PAYEE_CURRENT_BALANCE = new BigDecimal("14256");
    private final static Long TRANSACTION_ID = 0L;
    private final static String IBAN_TO_IBAN_TRANSACTION_TYPE = "IBAN_TO_IBAN";
    private final static BigDecimal TRANSACTION_AMOUNT = new BigDecimal(AMOUNT);

    private final static IbanToIbanRequest IBAN_TO_IBAN_REQUEST =
            IbanToIbanRequest.builder()
                    .type(IBAN_TO_IBAN_TYPE)
                    .fromIban(PAYER_IBAN)
                    .toIban(PAYEE_IBAN)
                    .amount(AMOUNT)
                    .description(DESCRIPTION)
                    .build();

    private final static Account PAYER_ACCOUNT =
            Account.builder()
                    .accountId(PAYER_ACCOUNT_ID)
                    .accountNumber(PAYER_ACCOUNT_NUMBER)
                    .iban(PAYER_IBAN)
                    .cnp(PAYER_CNP)
                    .name(PAYER_NAME)
                    .currentBalance(PAYER_CURRENT_BALANCE)
                    .build();

    private final static Account PAYEE_ACCOUNT =
            Account.builder()
                    .accountId(PAYEE_ACCOUNT_ID)
                    .accountNumber(PAYEE_ACCOUNT_NUMBER)
                    .iban(PAYEE_IBAN)
                    .cnp(PAYEE_CNP)
                    .name(PAYEE_NAME)
                    .currentBalance(PAYEE_CURRENT_BALANCE)
                    .build();

    private final static Transaction PAYER_TRANSACTION =
            Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .transactionType(IBAN_TO_IBAN_TRANSACTION_TYPE)
                    .transactionAmount(TRANSACTION_AMOUNT)
                    .cnp(PAYER_CNP)
                    .payment(true)
                    .description(DESCRIPTION)
                    .build();

    private final static Transaction PAYEE_TRANSACTION =
            Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .transactionType(IBAN_TO_IBAN_TRANSACTION_TYPE)
                    .transactionAmount(TRANSACTION_AMOUNT)
                    .cnp(PAYEE_CNP)
                    .payment(false)
                    .description(DESCRIPTION)
                    .build();

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    private TransactionServiceImpl transactionService;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        transactionService =
                new TransactionServiceImpl(accountRepository, transactionRepository);
    }

    @Test
    public void sendMoneyIbanToIbanTest() {
        BigDecimal payerBalance = PAYER_CURRENT_BALANCE;
        BigDecimal payeeBalance = PAYEE_CURRENT_BALANCE;
        when(accountRepository.findByIban(PAYER_IBAN))
                .thenReturn(PAYER_ACCOUNT);

        when(accountRepository.findByIban(PAYEE_IBAN))
                .thenReturn(PAYEE_ACCOUNT);

        sendMoney();

        List<Account> accounts = transactionService
                .sendMoneyIbanToIban(IBAN_TO_IBAN_REQUEST);

        BigDecimal payerCurrentBalance = accounts.get(0).getCurrentBalance();
        BigDecimal payeeCurrentBalance = accounts.get(1).getCurrentBalance();
        assertEquals(payerBalance.subtract(TRANSACTION_AMOUNT), payerCurrentBalance);
        assertEquals(payeeBalance.add(TRANSACTION_AMOUNT), payeeCurrentBalance);
    }

    private void sendMoney() {
        when(accountRepository.save(PAYER_ACCOUNT))
                .thenReturn(PAYER_ACCOUNT);

        when(accountRepository.save(PAYEE_ACCOUNT))
                .thenReturn(PAYEE_ACCOUNT);

        when(transactionRepository.save(PAYER_TRANSACTION))
                .thenReturn(PAYER_TRANSACTION);

        when(transactionRepository.save(PAYEE_TRANSACTION))
                .thenReturn(PAYEE_TRANSACTION);
    }
}
