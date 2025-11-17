package com.botswanabank.project.model;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class StaffUser {
    private UUID id;
    private String role;
    private String fullName;
    private OffsetDateTime createdAt;
}
