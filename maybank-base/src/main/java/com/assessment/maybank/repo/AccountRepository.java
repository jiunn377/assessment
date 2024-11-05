package com.assessment.maybank.repo;

import com.assessment.maybank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(Long accountNumber);

    List<Account> findByCustomerId(long id);

    Optional<Account> findAccountByAccountNumber(Long accountNumber);
}