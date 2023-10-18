package com.mesofi.payments.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "row_id")
    private long id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "payment_date")
    private String paymentDate;

    @ManyToOne
    @JoinColumn(name = "fk_unit", nullable = false, updatable = false)
    private Unit unit;
}
