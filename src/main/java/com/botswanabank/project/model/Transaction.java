package com.botswanabank.project.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class Transaction {
    private UUID id;
    private UUID accountId;
    private String transactionType;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private String reference;
    private OffsetDateTime createdAt;
}
