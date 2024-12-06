package org.poo.solution;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

// Builder
@Data
public class Account {
    private List<Card> cards;
    private String type;
    @JsonProperty("IBAN")
    private String IBAN;
    @JsonIgnore
    private String alias;
    @JsonIgnore
    private String owner;
    private String currency;
    @JsonIgnore
    private String description;
    @JsonIgnore
    private String commerciants;
    @JsonIgnore
    private String status;
    @JsonIgnore
    private double minBalance;
    private double balance;
    @JsonIgnore
    private int interestRate;
    @JsonIgnore
    private int id;

    // Constructor to initialize the cards list
    public Account() {
        this.cards = new ArrayList<>();
    }

    // Builder class for Account
    public static class AccountBuilder {
        private List<Card> cards = new ArrayList<>();
        private String type;
        private String IBAN;
        private String alias;
        private String currency;
        private String owner;
        private String description;
        private String commerciants;
        private String status;
        private int interestRate;
        private double balance;
        private int id;
        private double minBalance;

        public AccountBuilder setAlias(String alias) {
            this.alias = alias;
            return this;
        }

        public AccountBuilder setCards(List<Card> cards) {
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
            account.alias = this.alias;
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
    public double getExchange(Object object, String from, String to, double amount) {
        double rate = object.getExchangeRate(from, to);
        return amount * rate;
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
    public boolean transferFunds(Account targetAccount, double donorAmount, double receiverAmount) {
        if (this.withdrawFunds(donorAmount)) {
            targetAccount.addFunds(receiverAmount);
            return true;
        }
        return false;
    }
}