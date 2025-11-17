package bw.co.pulabank.model;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class CurrentUser {
    private String email;
    private UserRole role;
    private Customer customer;
    private String staffName;
    private String sessionToken;

    public boolean isCustomer() { return role == UserRole.CUSTOMER; }
    public boolean isStaff() { return role.isStaff(); }
    public boolean canApprove() { return role.canApproveAccounts(); }
}