package com.assessment.maybank.batch;

import com.assessment.maybank.beanio.TransactionBeanIO;
import com.assessment.maybank.model.Account;
import com.assessment.maybank.model.Customer;
import com.assessment.maybank.model.Transaction;
import com.assessment.maybank.service.AccountService;
import com.assessment.maybank.service.CustomerService;
import com.assessment.maybank.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class TransactionItemWriter implements ItemWriter<TransactionBeanIO> {


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TransactionService transactionService;

    public TransactionItemWriter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void write(List<? extends TransactionBeanIO> transactions) throws Exception {
        for (TransactionBeanIO transactionBeanIo : transactions) {

            Long accountNumber = transactionBeanIo.getAccountNumber();
            Long customerNumber = transactionBeanIo.getCustomerNumber();

            //Validate account and customer
            Account account = accountService.getAccountByAccountNumber(accountNumber);
            Customer customer = customerService.getCustomerByCustomerNumber(customerNumber);

            if (account == null && customer == null) {
                log.error("Account or Customer not found for accountNumber: {} and customerId: {}", accountNumber, customerNumber);
                continue;
            }

            if (account.getAccountBalance().compareTo(transactionBeanIo.getTrxAmount()) < 0) {
                log.error("Insufficient balance for accountNumber: {} and customerId: {}", accountNumber, customerNumber);
                //CODE HERE : Log to batch report
                continue;
            }
            account.setAccountBalance(account.getAccountBalance()
                    .subtract(transactionBeanIo.getTrxAmount()));
            account.setUpdateDateTime(new Date());

            Transaction transaction = new Transaction();
            transaction.setTransactionAmount(transactionBeanIo.getTrxAmount());
            transaction.setDescription(transactionBeanIo.getDescription());
            transaction.setTransactionDate(transactionBeanIo.getTrxDate());
            transaction.setTransactionTime(transactionBeanIo.getTrxTime());
            transaction.setAccount(account);

            transactionService.executeTransaction(transaction);
        }
    }

}