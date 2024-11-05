package com.assessment.maybank.rest.model;

import lombok.Data;

@Data
public class AccountStatusUpdateReq {

    private Long accountNumber;

    private String newStatus;
}
