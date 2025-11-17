package bw.co.pulabank.model;

public enum UserRole {
    CUSTOMER(false, false, false),
    TELLER(true, false, false),
    MANAGER(true, true, true),
    ADMIN(true, true, true);

    private final boolean staff;
    private final boolean canApprove;
    private final boolean canGenerateReports;

    UserRole(boolean staff, boolean canApprove, boolean canGenerateReports) {
        this.staff = staff;
        this.canApprove = canApprove;
        this.canGenerateReports = canGenerateReports;
    }

    public boolean isStaff() { return staff; }
    public boolean canApproveAccounts() { return canApprove; }
    public boolean canGenerateReports() { return canGenerateReports; }
}