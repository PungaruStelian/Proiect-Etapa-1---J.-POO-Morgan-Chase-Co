package org.poo.solution;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

// Builder
@Data
public class Account {
    private List<Cards> cards;
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
    @JsonIgnore
    private String status;

    // Constructor to initialize the cards list
    public Account() {
        this.cards = new ArrayList<>();
    }

    // Builder class for Account
    public static class AccountBuilder {
        private List<Cards> cards = new ArrayList<>();
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
        private String status;

        public AccountBuilder setCards(List<Cards> cards) {
            this.cards = cards;
            return this;
        }

        public AccountBuilder setInterestRate(int interestRate) {
            this.interestRate = interestRate;
            return this;
        }

        public AccountBuilder setType(String type) {
            this.type = type;
            return this;
        }

        public AccountBuilder setIBAN(String IBAN) {
            this.IBAN = IBAN;
            return this;
        }

        public AccountBuilder setBalance(double balance) {
            this.balance = balance;
            return this;
        }

        public AccountBuilder setCurrency(String currency) {
            this.currency = currency;
            return this;
        }

        public AccountBuilder setOwner(String owner) {
            this.owner = owner;
            return this;
        }

        public AccountBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public AccountBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public AccountBuilder setMinBalance(double minBalance) {
            this.minBalance = minBalance;
            return this;
        }

        public AccountBuilder setCommerciants(String commerciants) {
            this.commerciants = commerciants;
            return this;
        }

        public AccountBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Account build() {
            Account account = new Account();
            account.cards = this.cards;
            account.interestRate = this.interestRate;
            account.type = this.type;
            account.IBAN = this.IBAN;
            account.balance = this.balance;
            account.currency = this.currency;
            account.owner = this.owner;
            account.id = this.id;
            account.description = this.description;
            account.minBalance = this.minBalance;
            account.commerciants = this.commerciants;
            account.status = this.status;
            return account;
        }
    }

    public void activate() {
        this.status = "active";
    }

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