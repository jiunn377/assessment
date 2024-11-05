package com.assessment.maybank.service;

import com.assessment.maybank.error.ResourceNotFoundException;
import com.assessment.maybank.model.Account;
import com.assessment.maybank.model.Customer;
import com.assessment.maybank.model.Transaction;
import com.assessment.maybank.repo.CustomerRepository;
import com.assessment.maybank.repo.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import com.assessment.maybank.repo.AccountRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private  AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    public List<Account> getAccountList() {
        List<Account> accountList = accountRepository.findAll();
        return accountList;
    }


    public Account getAccountByAccountNumber(Long accountNumber) {

        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);
        return account.orElse(null);

    }

    @Transactional
    public Account addAccount(Account account) {

        if (account.getCustomer() != null) {
            Optional<Customer> customerOpt = customerRepository.findById(account.getCustomer().getId());
            if (!customerOpt.isPresent()) {
                throw new EntityNotFoundException("Customer not found with ID: " + account.getCustomer().getId());
            }
        }

        return accountRepository.save(account);
    }

    @Transactional
    public Account updateAccountStatus(Long accountNumber, String newStatus) {
        Account account = accountRepository.findAccountByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        account.setAccountStatus(newStatus);
        account.setUpdateDateTime(new Date());

        try {
            return accountRepository.save(account);
        } catch (OptimisticLockingFailureException e) {
            throw new RuntimeException("Update failed due to concurrent modification. Please try again.");
        }
    }

    @Transactional
    public void deleteAccountById(long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        try {
            accountRepository.deleteById(accountId);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Could not delete account due to existing references or constraints.");
        }
    }


    @Transactional
    public void fundTransfer(Long fromAccountNumber,  Long toAccountNumber, BigDecimal amount) {
        try {

            Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber)
                    .orElseThrow(() -> new ResourceNotFoundException("From account not found"));

            Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
                    .orElseThrow(() -> new ResourceNotFoundException("To account not found"));

            // Check if the account has sufficient balance
            if (fromAccount.getAccountBalance().compareTo(amount) < 0) {
                throw new RuntimeException("Insufficient balance in the account.");
            }

            // Update the FROM account balances
            fromAccount.setAccountBalance(fromAccount.getAccountBalance().subtract(amount));
            fromAccount.setUpdateDateTime(new Date());
            accountRepository.save(fromAccount);

            // Update the TO account balances
            toAccount.setAccountBalance(toAccount.getAccountBalance().add(amount));
            toAccount.setUpdateDateTime(new Date());
            accountRepository.save(toAccount);

            // Add to transaction
            Transaction transaction = new Transaction();
            transaction.setAccount(fromAccount);
            transaction.setTransactionType("DEBIT");
            transaction.setTransactionAmount(amount);
            transaction.setDescription("FUND TRANSFER");
            transaction.setTransactionDate(new Date());
            transaction.setTransactionTime(new Date());
            transactionRepository.save(transaction);

        }catch (OptimisticLockingFailureException e) {
            throw new RuntimeException("Update failed due to concurrent modification. Please try again.");
        }
    }
}