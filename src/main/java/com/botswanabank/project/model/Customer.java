package com.botswanabank.project.model;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class Customer {
    private UUID id;
    private String customerType;
    private String firstName;
    private String surname;
    private String address;
    private String omang;
    private String phone;
    private String status;
    private OffsetDateTime createdAt;
}
