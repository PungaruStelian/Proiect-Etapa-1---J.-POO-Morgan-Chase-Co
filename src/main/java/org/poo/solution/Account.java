package org.poo.solution;

import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@NoArgsConstructor
public class Account {
    private List<BankCard> bankcards;
    private int interestRate;
    private String type;
    private String IBAN;
    private double balance;
    private String currency;
    private String owner;
    private int id;
    private String description;
    private double minBalance;
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