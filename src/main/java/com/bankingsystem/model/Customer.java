package com.bankingsystem.model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String customerID;
    private String firstName;
    private String surname;
    private String address;
    private String password; // For authentication
    private List<Account> accounts;

    public Customer(String firstName, String surname, String address, String password) {
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.password = password;
        this.accounts = new ArrayList<>();
    }

    // Getters and Setters

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
    }
}
