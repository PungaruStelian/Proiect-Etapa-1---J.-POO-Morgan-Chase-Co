package org.poo.solution;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Account {
    private List<Cards> cards = new ArrayList<>();
    @JsonIgnore
    private int interestRate;
    private String type;
    @JsonProperty("IBAN")
    private String IBAN;
    private double balance;
    private String currency;
    @JsonIgnore
    private String owner;
    @JsonIgnore
    private int id;
    @JsonIgnore
    private String description;
    @JsonIgnore
    private double minBalance;
    @JsonIgnore
    private String commerciants;

    // Method to add funds to the account
    public void addFunds(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    // Method to withdraw funds from the account
    public boolean withdrawFunds(double amount) {
        if (amount > 0 && this.balance >= amount) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    // Method to transfer funds to another account
    public boolean transferFunds(Account targetAccount, double amount) {
        if (this.withdrawFunds(amount)) {
            targetAccount.addFunds(amount);
            return true;
        }
        return false;
    }
}