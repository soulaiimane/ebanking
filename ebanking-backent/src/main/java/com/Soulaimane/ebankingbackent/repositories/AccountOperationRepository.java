package com.Soulaimane.ebankingbackent.repositories;

import com.Soulaimane.ebankingbackent.entities.AccountOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountOperationRepository extends JpaRepository<AccountOperation,Long> {
   public List<AccountOperation> findByAccountId(String accountId);
   public Page<AccountOperation> findByAccountIdOrderByOperationDateDesc(String accountId, Pageable pageable);
}
