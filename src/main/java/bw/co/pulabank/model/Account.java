package bw.co.pulabank.model;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private UUID id;
    private UUID customerId;
    
    private AccountType accountType;
    private String accountNumber;        // e.g., 600012345678
    private Double balance = 0.0;
    
    // AM-005 & AM-006
    private AccountStatus status = AccountStatus.PENDING;
    
    private Double interestRate;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
    
    // For Cheque accounts only
    private String employmentProofUrl;   // Supabase Storage link
    private String employmentProofFileName;

    // Auto-calculated
    public boolean requiresEmploymentProof() {
        return accountType == AccountType.CHEQUE;
    }

    public boolean hasMinimumBalance() {
        return accountType == AccountType.INVESTMENT && balance < 500.0;
    }

    public String getFormattedBalance() {
        return String.format("BWP %,.2f", balance);
    }

    public boolean isActive() {
        return status == AccountStatus.ACTIVE;
    }
}