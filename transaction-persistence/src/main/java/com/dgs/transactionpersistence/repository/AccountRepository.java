package com.dgs.transactionpersistence.repository;

import com.dgs.transactionpersistence.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByIban(String iban);

    Account findByAccountNumber(String accountNumber);

    Account findByCnp(String cnp);
}
