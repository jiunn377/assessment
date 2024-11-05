package com.assessment.maybank.controller;

import com.assessment.maybank.error.CustomConcurrencyException;
import com.assessment.maybank.model.Customer;
import com.assessment.maybank.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/customer")
@Tag(name = "Customer Controller")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Operation(summary = "List customers")
    @GetMapping("/all")
    public ResponseEntity<List<Customer>>customerListInquiry() throws Exception{

        List<Customer> response = customerService.getCustomerList();
        return  new ResponseEntity<>(response, HttpStatus.OK);

    }


    @Operation(summary = "Get customer")
    @GetMapping("/{customerNumber}")
    public ResponseEntity<Customer> customerDetailInquiry(
            @Parameter(description = "Customer Number", example = "111")
            @RequestParam Long customerNumber) throws Exception{

        Customer customer = customerService.getCustomerByCustomerNumber(customerNumber);
        return new ResponseEntity<>(customer, HttpStatus.OK);

    }

    @Operation(summary = "Add new customer")
    @PostMapping("/add")
    public ResponseEntity<Object> addCustomer(@RequestBody Customer customer) {
        try {

            Customer savedCustomer = customerService.addCustomer(customer);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation error: " + e.getMessage());

        }catch (RuntimeException e) {
            log.error("Error adding customer: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @Operation(summary = "Update customer")
    @PostMapping("/update")
    public ResponseEntity<Object> updateCustomer(@RequestBody Customer customer) {

        try {
            Customer updatedCustomer = customerService.updateCustomer(customer);
            return ResponseEntity.ok(updatedCustomer);

        } catch (CustomConcurrencyException e ) {
            log.error("Error concurrent modification: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Update failed due to a concurrent modification");

        } catch (ObjectOptimisticLockingFailureException e) {
            log.error("Error concurrent modification: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Update failed due to a concurrent modification");

        } catch (RuntimeException e) {
            log.error("Error updating customer: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


    @Operation(summary = "Delete customer")
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable int customerId) {

        try {
            customerService.deleteCustomerByCustomerNumber(customerId);
            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (EntityNotFoundException e) {
            log.error("Customer not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (RuntimeException e) {
            log.error("Error deleting customer: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
    }

}

