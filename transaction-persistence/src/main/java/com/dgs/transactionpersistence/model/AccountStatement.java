package com.dgs.transactionpersistence.model;

import com.dgs.transactionpersistence.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AccountStatement {
    private String name;
    private String cnp;
    private String iban;
    private BigDecimal currentBalance;
    private List<Transaction> transactionHistory;
}
