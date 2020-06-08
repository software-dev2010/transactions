package com.dgs.transactionpersistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accountId")
    private Long accountId;

    @Column(name = "accountNumber")
    private String accountNumber;

    @Column(name = "iban")
    private String iban;

    @Column(name = "cnp")
    private String cnp;

    @Column(name = "name")
    private String name;

    @Column(name = "currentBalance")
    private BigDecimal currentBalance;
}
