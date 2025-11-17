package com.botswanabank.project.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class Account {
    private UUID id;
    private UUID customerId;
    private String accountType;
    private String accountNumber;
    private BigDecimal balance;
    private String status;
    private BigDecimal interestRate;
    private BigDecimal minBalance;
    private OffsetDateTime openedAt;
}
