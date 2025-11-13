
package com.bankingsystem.model;

public class BankStaff {

    private int id;
    private String firstName;
    private String surname;
    private String idNumber;
    private String role;

    public BankStaff(int id, String firstName, String surname, String idNumber, String role) {
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
        this.idNumber = idNumber;
        this.role = role;
    }

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

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
