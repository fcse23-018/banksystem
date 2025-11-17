package bw.co.pulabank.model;

public enum AccountType {
    SAVINGS(0.0005, 0.0, false),        // 0.05% monthly
    INVESTMENT(0.05, 500.0, false),     // 5% monthly, min BWP 500
    CHEQUE(0.0, 0.0, true);             // No interest, needs employment proof

    private final double monthlyInterestRate;
    private final double minimumBalance;
    private final boolean requiresEmploymentProof;

    AccountType(double monthlyInterestRate, double minimumBalance, boolean requiresEmploymentProof) {
        this.monthlyInterestRate = monthlyInterestRate;
        this.minimumBalance = minimumBalance;
        this.requiresEmploymentProof = requiresEmploymentProof;
    }

    public double getMonthlyInterestRate() { return monthlyInterestRate; }
    public double getMinimumBalance() { return minimumBalance; }
    public boolean requiresEmploymentProof() { return requiresEmploymentProof; }
    public boolean hasMinimumBalanceRequirement() { return minimumBalance > 0; }
}