package com.assessment.maybank_api;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.assessment.maybank.MaybankMainApplication;
import com.assessment.maybank.error.CustomConcurrencyException;
import com.assessment.maybank.model.Customer;
import com.assessment.maybank.repo.CustomerRepository;
import com.assessment.maybank.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.concurrent.*;

@SpringBootTest(classes = MaybankMainApplication.class)
public class ConcurrentUpdateCustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testConcurrentUpdate() throws InterruptedException, ExecutionException {

        Callable<Customer> task1 = () -> {
            Customer customer1 = customerRepository.findById(1L).orElseThrow();
            customer1.setCustomerFirstName("updated_test_name1");
            return customerService.updateCustomer(customer1);
        };

        Callable<Customer> task2 = () -> {
            Customer customer2 = customerRepository.findById(1L).orElseThrow();
            customer2.setCustomerFirstName("updated_test_name2");
            return customerService.updateCustomer(customer2);
        };

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<Customer> future1 = executor.submit(task1);
        Future<Customer> future2 = executor.submit(task2);

        try {
            future1.get();
            future2.get();

        } catch (ExecutionException e) {

            if (e.getCause() instanceof CustomConcurrencyException) {
                System.out.println("CustomConcurrencyException caught as expected.");

            }else if (e.getCause() instanceof ObjectOptimisticLockingFailureException) {
                System.out.println("ObjectOptimisticLockingFailureException caught as expected.");

            } else {
                throw e;
            }
        } finally {
            executor.shutdown();
        }
    }
}
