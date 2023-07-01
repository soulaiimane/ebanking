package com.Soulaimane.ebankingbackent.repositories;

import com.Soulaimane.ebankingbackent.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
}
