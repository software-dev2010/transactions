package com.dgs.transactionpersistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Column(name = "transactionType")
    private String transactionType;

    @Column(name = "cnp")
    private String cnp;

    @Column(name = "transactionAmount")
    private BigDecimal transactionAmount;

    @Column(name = "payment")
    private boolean payment;

    @Column(name = "description")
    private String description;
}
