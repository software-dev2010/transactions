package com.dgs.transactionpersistence.repository;

import com.dgs.transactionpersistence.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCnp(String accountNumber);
}