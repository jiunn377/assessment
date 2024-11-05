package com.assessment.maybank.service;

import com.assessment.maybank.model.Account;
import com.assessment.maybank.model.Transaction;
import com.assessment.maybank.repo.AccountRepository;
import com.assessment.maybank.repo.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    private final String transactionType = "DEBIT";

    @Transactional
    public void executeTransaction(Transaction transaction) {

        transaction.setTransactionType(transactionType);
        transactionRepository.save(transaction);
    }


    public Page<Transaction> inquireTransactions(Long accountNumber, Pageable pageable) {
        return transactionRepository.findByAccount_AccountNumberAndTransactionType(accountNumber, transactionType, pageable);
    }


    public Page<Transaction> findTransactionsByDescription(String description, PageRequest pageable) {
        return transactionRepository.findByDescriptionContainingAndTransactionType(description, transactionType, pageable);
    }
}