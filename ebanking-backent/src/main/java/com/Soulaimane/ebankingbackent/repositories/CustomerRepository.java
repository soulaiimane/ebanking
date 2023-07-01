package com.Soulaimane.ebankingbackent.repositories;

import com.Soulaimane.ebankingbackent.dtos.CustomerDto;
import com.Soulaimane.ebankingbackent.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    @Query("select c from Customer c where c.name like :keyword")
    public List<Customer> searchCustomer(@Param("keyword") String keyword);
}
