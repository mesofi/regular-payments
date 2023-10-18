package com.mesofi.payments.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.mesofi.payments.entity.Payment;

@RepositoryRestResource(collectionResourceRel = "payments", path = "payments")
public interface PaymentRepository extends PagingAndSortingRepository<Payment, Long>,CrudRepository<Payment, Long> {

}