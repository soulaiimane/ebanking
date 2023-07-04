package com.Soulaimane.ebankingbackent.repositories;

import com.Soulaimane.ebankingbackent.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
    public List<BankAccount> findBankAccountsByCustomerId(Long customerId);
}
