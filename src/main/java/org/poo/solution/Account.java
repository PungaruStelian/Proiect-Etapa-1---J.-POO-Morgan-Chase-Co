package org.poo.solution;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

@Data
public class Account {
    private List<AbstractCard> cards;
    private String type;
    @JsonProperty("IBAN")
    private String iban;
    @JsonIgnore
    private String alias;
    private String currency;
    @JsonIgnore
    private String status;
    @JsonIgnore
    private double minBalance;
    private double balance;
    @JsonIgnore
    private double interestRate;

    /**
     * Constructor for Account
     */
    public Account() {
        this.cards = new ArrayList<>();
    }

    /**
     * Builder class for Accounts
     */
    public static class AccountBuilder {
        private String type;
        private String iban;
        private String currency;
        private String status;
        private double balance;

        /**
         * Method to set the type for the account
         * @param otherType The type to set
         * @return The AccountBuilder object
         */
        public AccountBuilder setType(final String otherType) {
            type = otherType;
            return this;
        }

        /**
         * Method to set the iban for the account
         * @param otherIban The iban to set
         * @return The AccountBuilder object
         */
        public AccountBuilder setIBAN(final String otherIban) {
            this.iban = otherIban;
            return this;
        }

        /**
         * Method to set the balance for the account
         * @param otherBalance The balance to set
         * @return The AccountBuilder object
         */
        public AccountBuilder setBalance(final double otherBalance) {
            this.balance = otherBalance;
            return this;
        }

        /**
         * Method to set the currency for the account
         * @param otherCurrency The currency to set
         * @return The AccountBuilder object
         */
        public AccountBuilder setCurrency(final String otherCurrency) {
            this.currency = otherCurrency;
            return this;
        }

        /**
         * Method to set the status for the account
         * @param otherStatus The status to set
         * @return The AccountBuilder object
         */
        public AccountBuilder setStatus(final String otherStatus) {
            this.status = otherStatus;
            return this;
        }

        /**
         * Method to build the account
         * @return The Account object
         */
        public Account build() {
            Account account = new Account();
            account.type = this.type;
            account.iban = this.iban;
            account.balance = this.balance;
            account.currency = this.currency;
            account.status = this.status;
            return account;
        }
    }

    /**
     * Method to add funds to the account
     * @param amount The amount to add
     */
    public void addFunds(final double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    /**
     * Method to get the exchange rate between two currencies
     * @param object The object containing the exchange rates
     * @param from The currency to convert from
     * @param to The currency to convert to
     * @return The exchange rate
     */
    public double getExchange(final Object object, final String from, final String to,
                              final double amount) {
        double rate = object.getExchangeRate(from, to);
        return amount * rate;
    }

    /**
     * Method to check if the account is frozen
     * @return true if the account is frozen, false otherwise
     */
    public boolean withdrawFunds(final double amount) {
        if (amount > 0 && this.balance >= amount) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    /**
     * Method to transfer funds from one account to another
     * @param targetAccount The account to transfer the funds to
     * @param donorAmount The amount to withdraw from the current account
     * @param receiverAmount The amount to add to the target account
     * @return true if the transfer was successful, false otherwise
     */
    public boolean transferFunds(final Account targetAccount, final double donorAmount,
                                 final double receiverAmount) {
        if (this.withdrawFunds(donorAmount)) {
            targetAccount.addFunds(receiverAmount);
            return true;
        }
        return false;
    }
}
