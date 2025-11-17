package bw.co.pulabank.model;

public class CurrentUser {
    private static Customer customer;

    public static Customer getCustomer() {
        return customer;
    }

    public static void setCustomer(Customer customer) {
        CurrentUser.customer = customer;
    }
}
