package bw.co.pulabank.model;

public enum AccountStatus {
    PENDING,      // Awaiting staff approval
    ACTIVE,
    SUSPENDED,
    CLOSED        // Staff-approved closure, balance = 0
}