package bw.co.pulabank.model;

public enum UserRole {
    CUSTOMER,
    TELLER,
    MANAGER,
    ADMIN;

    public boolean isStaff() {
        return this != CUSTOMER;
    }

    public boolean canApproveAccounts() {
        return this == MANAGER || this == ADMIN;
    }

    public boolean canGenerateReports() {
        return this == MANAGER || this == ADMIN;
    }
}