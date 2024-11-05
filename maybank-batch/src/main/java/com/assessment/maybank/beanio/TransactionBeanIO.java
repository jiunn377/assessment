package com.assessment.maybank.beanio;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransactionBeanIO {
	
    private Long accountNumber;
    private BigDecimal trxAmount;
    private String description;
    private Date trxDate;
    private Date trxTime; 
    private Long customerNumber;
    
}