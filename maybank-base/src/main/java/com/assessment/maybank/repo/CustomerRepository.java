package com.assessment.maybank.repo;

import com.assessment.maybank.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByCustomerNumber(Long customerNumber);

    boolean existsByCustomerNumber(Long customerNumber);

    void deleteByCustomerNumber(Long customerNumber);

}