package com.assessment.maybank.rest.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FundTransferReq {

    Long fromAccountNumber;
    Long toAccountNumber;
    BigDecimal amount;

}
