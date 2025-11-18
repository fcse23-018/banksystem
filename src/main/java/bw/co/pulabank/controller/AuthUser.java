package bw.co.pulabank.controller;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
// Simple controller placeholder removed; AuthUser model now lives in bw.co.pulabank.model package
public class AuthUserController {
    private String email;
    private UserRole role;
    private Customer customer;  // Only for customers
    private String staffName;   // Only for staff

    public boolean isCustomer() {
        return role == UserRole.CUSTOMER;
    }

    public boolean isStaff() {
        return !isCustomer();
    }

    public boolean canApproveAccounts() {
        return role != null && role.canApproveAccounts();
    }
}
