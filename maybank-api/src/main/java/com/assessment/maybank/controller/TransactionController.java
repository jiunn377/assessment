package com.assessment.maybank.controller;

import com.assessment.maybank.model.Transaction;
import com.assessment.maybank.service.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/transaction")
@Tag(name = "Transaction Controller")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/transactionListInquiry")
    public ResponseEntity<Page<Transaction>> getTransactionsByAccountNumber(
            @RequestParam Long accountNumber,
            @RequestParam int page,
            @RequestParam int size) {

        PageRequest pageable = PageRequest.of(page, size);
        Page<Transaction> response = transactionService.inquireTransactions(accountNumber, pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/filterByDescription")
    public ResponseEntity<Page<Transaction>> findTransactionsByDescription(
            @RequestParam String description,
            @RequestParam int page,
            @RequestParam int size) {

        PageRequest pageable = PageRequest.of(page, size);
        Page<Transaction> response = transactionService.findTransactionsByDescription(description, pageable);

        return ResponseEntity.ok(response);
    }

}