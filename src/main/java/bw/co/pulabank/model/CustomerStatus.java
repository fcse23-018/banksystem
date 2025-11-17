package bw.co.pulabank.model;

public enum CustomerStatus {
    PENDING,      // After registration, awaiting verification
    ACTIVE,       // Fully approved
    SUSPENDED,    // Temporarily blocked
    CLOSED        // Permanently closed
}