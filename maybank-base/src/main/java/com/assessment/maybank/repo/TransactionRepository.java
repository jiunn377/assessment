package com.assessment.maybank.repo;

import com.assessment.maybank.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByDescriptionContainingAndTransactionType(String description,String transactionType, Pageable pageable);

    Page<Transaction> findByAccount_AccountNumberAndTransactionType(Long accountNumber,String transactionType, Pageable pageable);

}