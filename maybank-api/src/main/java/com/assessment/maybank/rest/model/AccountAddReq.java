package com.assessment.maybank.rest.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountAddReq {

    private Long accountNumber;
    private String accountStatus;
    private String accountType;
    private BigDecimal accountBalance;
    private Long customerNumber;
}
