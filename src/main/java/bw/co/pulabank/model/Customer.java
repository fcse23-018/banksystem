package bw.co.pulabank.model;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"omang", "address"}) // Never log sensitive data
public class Customer {
    private UUID id;
    
    // CM-003: Multiple customer types
    private CustomerType customerType;
    
    private String firstName;
    private String surname;
    private String address;        // Encrypted in DB via pgcrypto
    private String omang;          // Botswana National ID - Encrypted
    private String email;
    private String phone;
    
    // Status flow: pending → active → suspended
    private CustomerStatus status = CustomerStatus.PENDING;
    
    private LocalDateTime createdAt;
    private LocalDateTime verifiedAt;

    public String getFullName() {
        return firstName + " " + surname;
    }

    public boolean isActive() {
        return status == CustomerStatus.ACTIVE;
    }

    public boolean isPendingVerification() {
        return status == CustomerStatus.PENDING;
    }
}