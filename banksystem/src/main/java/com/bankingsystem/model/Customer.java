package com.bankingsystem.model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private int id;
    private String firstName;
    private String surname;
    private String address;
    private String idNumber;
    private String status;
    private String password; // For authentication
    private List<Account> accounts;

    public Customer(String firstName, String surname, String address, String idNumber, String password) {
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.idNumber = idNumber;
        this.password = password;
        this.accounts = new ArrayList<>();
    }

    public Customer(int id, String firstName, String surname, String address, String idNumber, String status, String password) {
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.idNumber = idNumber;
        this.status = status;
        this.password = password;
        this.accounts = new ArrayList<>();
    }


    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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