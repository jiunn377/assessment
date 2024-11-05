package com.assessment.maybank.service;

import com.assessment.maybank.error.CustomConcurrencyException;
import com.assessment.maybank.error.ResourceNotFoundException;
import com.assessment.maybank.model.Account;
import com.assessment.maybank.model.Customer;
import com.assessment.maybank.repo.AccountRepository;
import com.assessment.maybank.repo.CustomerRepository;
import org.hibernate.StaleStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    public List<Customer> getCustomerList() {
        List<Customer> customerList = customerRepository.findAll();
        return customerList;
    }


    public Customer getCustomerByCustomerNumber(Long customerNumber) {
        Optional<Customer> customer = customerRepository.findByCustomerNumber(customerNumber);
        return customer.orElse(null);
    }


    public Customer addCustomer(Customer customer) {
        customer.setCreateDateTime(new Date());
        if (customerRepository.existsByCustomerNumber(customer.getCustomerNumber())) {
            throw new IllegalArgumentException("Customer with this number already exists.");
        }

        return customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomer(Customer customer) {
        try {
            return customerRepository.save(customer);

        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Update failed due to duplicate entry for customer number: " + customer.getCustomerNumber());
        } catch (OptimisticLockingFailureException e) {
            throw new CustomConcurrencyException("Update failed due to a concurrent modification.");
        }
    }

    @Transactional
    public void deleteCustomerByCustomerNumber(long customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + customerId));

        try {
            customerRepository.deleteById(customerId);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Could not delete customer due to existing references or constraints.");
        }
    }
}