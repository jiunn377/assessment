package com.assessment.maybank.controller;


import com.assessment.maybank.error.ResourceNotFoundException;
import com.assessment.maybank.model.Account;
import com.assessment.maybank.model.Customer;
import com.assessment.maybank.rest.model.AccountAddReq;
import com.assessment.maybank.rest.model.AccountStatusUpdateReq;
import com.assessment.maybank.rest.model.FundTransferReq;
import com.assessment.maybank.service.AccountService;
import com.assessment.maybank.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/account")
@Tag(name = "Batch Controller")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerService customerService;



    @Operation(summary = "List all account")
    @GetMapping("/all")
    public ResponseEntity<List<Account>>customerListInquiry() throws Exception{

        List<Account> response = accountService.getAccountList();
        return  new ResponseEntity<>(response, HttpStatus.OK);

    }


    @Operation(summary = "Get account details")
    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccountByAccountNumber(
            @Parameter(description = "Account Number", example = "6872838260")
            @RequestParam Long accountNumber) {

        Account account = accountService.getAccountByAccountNumber(accountNumber);
        return ResponseEntity.ok(account);

    }


    @Operation(summary = "Add account")
    @PostMapping("/add")
    public ResponseEntity<Account> addAccount(@RequestBody AccountAddReq accountAddReq) {
        log.info("Calling Account Detail Inquiry API");

        Account account = new Account();
        account.setAccountNumber(accountAddReq.getAccountNumber());
        account.setAccountStatus(accountAddReq.getAccountStatus());
        account.setAccountType(accountAddReq.getAccountType());
        account.setAccountBalance(accountAddReq.getAccountBalance());

        // Validate the customer exists
        Customer customer = customerService.getCustomerByCustomerNumber(accountAddReq.getCustomerNumber());
        if (customer == null) {
            throw new EntityNotFoundException("Customer not found");
        }

        account.setCustomer(customer);
        Account newAccount = accountService.addAccount(account);

        return ResponseEntity.status(HttpStatus.CREATED).body(newAccount);
    }


    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable int accountId) {
        accountService.deleteAccountById(accountId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @PostMapping("/accountStatusUpdate")
    public ResponseEntity<Object> updateAccountStatus(@RequestBody AccountStatusUpdateReq accountStatusUpdateReq) {

        try{
            Account account = accountService.updateAccountStatus(accountStatusUpdateReq.getAccountNumber(), accountStatusUpdateReq.getNewStatus());
            return ResponseEntity.status(HttpStatus.OK).body(account);

        } catch (ResourceNotFoundException e) {

            log.error("Account not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (OptimisticLockingFailureException e) {

            log.error("Optimistic lock failure: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The account was updated by another transaction. Please retry your update.");

        } catch (DataIntegrityViolationException e) {

            log.error("Data integrity violation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (Exception e) {

            log.error("Unexpected error occurred: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> fundTransfer(@RequestBody FundTransferReq fundTransferReq) {

        Long fromAccountNumber = fundTransferReq.getFromAccountNumber();
        Long toAccountNumber = fundTransferReq.getToAccountNumber();
        BigDecimal amount = fundTransferReq.getAmount();

        accountService.fundTransfer(fromAccountNumber, toAccountNumber, amount);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
