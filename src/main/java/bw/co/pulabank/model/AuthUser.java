package bw.co.pulabank.model;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuthUser {
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
