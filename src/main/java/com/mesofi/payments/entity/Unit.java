package com.mesofi.payments.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Greg Turnquist
 */
@Getter
@Setter
//@Entity
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "row_id")
    private long id;

    @Column(name = "number")
    private String number;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "unit")
    private List<Payment> payments;

    @Override
    public String toString() {
        return number;
    }
}
