package com.dgs.transactionpersistence.service;

import com.dgs.transactionpersistence.entity.Account;
import com.dgs.transactionpersistence.model.AccountStatement;
import com.dgs.transactionpersistence.request.*;

import java.util.List;

public interface TransactionService {

    List<Account> sendMoneyIbanToIban(IbanToIbanRequest ibanToIbanRequest);

    List<Account> sendMoneyIbanToWallet(IbanToWalletRequest ibanToWalletRequest);

    List<Account> sendMoneyWalletToIban(WalletToIbanRequest walletToIbanRequest);

    List<Account> sendMoneyWalletToWallet(WalletToWalletRequest walletToWalletRequest);

    AccountStatement getStatement(AccountStatementRequest accountStatementRequest);
}
