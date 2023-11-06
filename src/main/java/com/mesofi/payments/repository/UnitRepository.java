package com.mesofi.payments.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.mesofi.payments.entity.Unit;

//@RepositoryRestResource(collectionResourceRel = "units", path = "units")
public interface UnitRepository{// extends PagingAndSortingRepository<Unit, Long>, CrudRepository<Unit, Long> {

    List<Unit> findByNumber(@Param("number") String number);

}