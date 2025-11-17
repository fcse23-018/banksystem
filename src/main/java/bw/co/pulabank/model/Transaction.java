package bw.co.pulabank.model;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private UUID id;
    private UUID accountId;
    private UUID fromAccountId;      // For transfers
    private UUID toAccountId;        // For transfers
    
    private TransactionType type;
    private Double amount;
    private Double balanceAfter;
    private String reference;
    private String description;
    
    private LocalDateTime createdAt;

    public boolean isCredit() {
        return type == TransactionType.DEPOSIT ||
               type == TransactionType.TRANSFER_IN ||
               type == TransactionType.INTEREST_CREDIT;
    }

    public boolean isDebit() {
        return type == TransactionType.WITHDRAWAL ||
               type == TransactionType.TRANSFER_OUT;
    }

    public String getDisplayAmount() {
        String sign = isCredit() ? "+" : "-";
        return sign + " BWP " + String.format("%,.2f", Math.abs(amount));
    }
}