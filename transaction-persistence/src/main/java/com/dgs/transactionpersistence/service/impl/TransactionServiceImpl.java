package com.dgs.transactionpersistence.service.impl;

import com.dgs.transactionpersistence.entity.Account;
import com.dgs.transactionpersistence.entity.Transaction;
import com.dgs.transactionpersistence.model.AccountStatement;
import com.dgs.transactionpersistence.repository.AccountRepository;
import com.dgs.transactionpersistence.repository.TransactionRepository;
import com.dgs.transactionpersistence.request.*;
import com.dgs.transactionpersistence.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.dgs.transactionpersistence.util.TransactionsConstants.*;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public List<Account> sendMoneyIbanToIban(IbanToIbanRequest ibanToIbanRequest) {
        String fromIban = ibanToIbanRequest.getFromIban();
        String toIban = ibanToIbanRequest.getToIban();
        BigDecimal amount = new BigDecimal(ibanToIbanRequest.getAmount());
        Account fromAccount = accountRepository.findByIban(fromIban);
        Account toAccount = accountRepository.findByIban(toIban);
        if (fromAccount.getCurrentBalance().compareTo(amount) == 1) {
            String description = ibanToIbanRequest.getDescription();
            return sendMoney(fromAccount, toAccount, amount, description,
                    IBAN_TO_IBAN_TRANSACTION_TYPE);
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional
    public List<Account> sendMoneyIbanToWallet(IbanToWalletRequest ibanToWalletRequest) {
        String fromIban = ibanToWalletRequest.getFromIban();
        String toWallet = ibanToWalletRequest.getToWalletNumber();
        BigDecimal amount = new BigDecimal(ibanToWalletRequest.getAmount());
        Account fromAccount = accountRepository.findByIban(fromIban);
        Account toAccount = accountRepository.findByAccountNumber(toWallet);
        if (fromAccount.getCurrentBalance().compareTo(amount) == 1) {
            String description = ibanToWalletRequest.getDescription();
            return sendMoney(fromAccount, toAccount, amount, description,
                    IBAN_TO_WALLET_TRANSACTION_TYPE);
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional
    public List<Account> sendMoneyWalletToIban(WalletToIbanRequest walletToIbanRequest) {
        String fromWallet = walletToIbanRequest.getFromWalletNumber();
        String toIban = walletToIbanRequest.getToIban();
        BigDecimal amount = new BigDecimal(walletToIbanRequest.getAmount());
        Account fromAccount = accountRepository.findByAccountNumber(fromWallet);
        Account toAccount = accountRepository.findByIban(toIban);
        if (fromAccount.getCurrentBalance().compareTo(amount) == 1) {
            String description = walletToIbanRequest.getDescription();
            return sendMoney(fromAccount, toAccount, amount, description,
                    WALLET_TO_IBAN_TRANSACTION_TYPE);
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional
    public List<Account> sendMoneyWalletToWallet(WalletToWalletRequest walletToWalletRequest) {
        String fromWallet = walletToWalletRequest.getFromWalletNumber();
        String toWallet = walletToWalletRequest.getToWalletNumber();
        BigDecimal amount = new BigDecimal(walletToWalletRequest.getAmount());
        Account fromAccount = accountRepository.findByAccountNumber(fromWallet);
        Account toAccount = accountRepository.findByAccountNumber(toWallet);
        if (fromAccount.getCurrentBalance().compareTo(amount) == 1) {
            String description = walletToWalletRequest.getDescription();
            return sendMoney(fromAccount, toAccount, amount, description,
                    WALLET_TO_WALLET_TRANSACTION_TYPE);
        }
        return new ArrayList<>();
    }

    private List<Account> sendMoney(
            Account fromAccount, Account toAccount, BigDecimal amount,
            String description, String transactionType) {

        List<Account> accounts = new ArrayList<>();
        fromAccount.setCurrentBalance(fromAccount.getCurrentBalance().subtract(amount));
        Account payerAccount = accountRepository.save(fromAccount);
        toAccount.setCurrentBalance(toAccount.getCurrentBalance().add(amount));
        Account payeeAccount = accountRepository.save(toAccount);
        accounts.add(payerAccount);
        accounts.add(payeeAccount);
        String cnpFrom = fromAccount.getCnp();
        String cnpTo = toAccount.getCnp();
        transactionRepository.save(
                Transaction.builder()
                        .transactionId(0L)
                        .transactionType(transactionType)
                        .cnp(cnpFrom)
                        .transactionAmount(amount)
                        .payment(true)
                        .description(description)
                        .build());

        transactionRepository.save(
                Transaction.builder()
                        .transactionId(0L)
                        .transactionType(transactionType)
                        .cnp(cnpTo)
                        .transactionAmount(amount)
                        .description(description)
                        .build());

        return accounts;
    }

    @Override
    public AccountStatement getStatement(AccountStatementRequest accountStatementRequest) {
        String cnp = accountStatementRequest.getCnp();
        Account account = accountRepository.findByCnp(cnp);
        return AccountStatement.builder()
                .name(account.getName())
                .cnp(account.getCnp())
                .iban(account.getIban())
                .currentBalance(account.getCurrentBalance())
                .transactionHistory(transactionRepository.findByCnp(cnp))
                .build();
    }
}
