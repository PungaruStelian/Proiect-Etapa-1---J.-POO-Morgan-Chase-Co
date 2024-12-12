package org.poo.solution;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SavingsAccount implements AccountType {
    @JsonProperty("IBAN")
    private String iban;
    private String currency;
    @JsonIgnore
    private String alias;
    private double balance;
    @JsonIgnore
    private double minBalance;
    @JsonIgnore
    private double interestRate;
    private List<AbstractCard> cards;

    public SavingsAccount() {
        this.cards = new ArrayList<>();
    }

    public static class SavingsAccountBuilder {
        private String iban;
        private String currency;
        private double balance;
        private String alias;
        private double interestRate;

        /**
         * Method to set the alias of the account
         * @param otherAlias The alias of the account
         * @return The SavingsAccountBuilder object
         */
        public SavingsAccountBuilder setAlias(final String otherAlias) {
            this.alias = otherAlias;
            return this;
        }

        /**
         * Method to set the IBAN of the account
         * @param otherIban The IBAN of the account
         * @return The SavingsAccountBuilder object
         */
        public SavingsAccountBuilder setIban(final String otherIban) {
            this.iban = otherIban;
            return this;
        }

        /**
         * Method to set the currency of the account
         * @param otherCurrency The currency of the account
         * @return The SavingsAccountBuilder object
         */
        public SavingsAccountBuilder setCurrency(final String otherCurrency) {
            this.currency = otherCurrency;
            return this;
        }

        /**
         * Method to set the balance of the account
         * @param otherBalance The balance of the account
         * @return The SavingsAccountBuilder object
         */
        public SavingsAccountBuilder setBalance(final double otherBalance) {
            this.balance = otherBalance;
            return this;
        }

        /**
         * Method to set the interest rate of the account
         * @param otherInterestRate The interest rate of the account
         * @return The SavingsAccountBuilder object
         */
        public SavingsAccountBuilder setInterestRate(final double otherInterestRate) {
            this.interestRate = otherInterestRate;
            return this;
        }

        /**
         * Method to build the SavingsAccount object
         * @return The SavingsAccount object
         */
        public SavingsAccount build() {
            SavingsAccount savingsAccount = new SavingsAccount();
            savingsAccount.iban = this.iban;
            savingsAccount.currency = this.currency;
            savingsAccount.balance = this.balance;
            savingsAccount.alias = this.alias;
            savingsAccount.interestRate = this.interestRate;
            return savingsAccount;
        }

    }

    /**
     * Method to get the alias of the account
     * @return The alias of the account
     */
    @Override
    public String getType() {
        return "savings";
    }

    /**
     * Method to get the alias of the account
     * @return The alias of the account
     */
    @Override
    public String getIban() {
        return iban;
    }

    /**
     * Method to get the currency of the account
     * @return The currency of the account
     */
    @Override
    public double getBalance() {
        return balance;
    }

    /**
     * Method to add funds to the account
     * @param amount The amount to add
     */
    @Override
    public void addFunds(final double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    /**
     * Method to get the alias of the account
     * @return The alias of the account
     */
    @Override
    public boolean withdrawFunds(final double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    /**
     * Method to get the exchange rate
     * @param object The object to get the exchange rate from
     * @param from The currency to exchange from
     * @param to The currency to exchange to
     * @param amount The amount to exchange
     * @return The exchange rate
     */
    @Override
    public double getExchange(final Object object, final String from, final String to,
                              final double amount) {
        double rate = object.getExchangeRate(from, to);
        return amount * rate;
    }

    /**
     * Method to get the cards of the account
     * @return The cards of the account
     */
    @Override
    public boolean transferFunds(final AccountType targetAccount, final double donorAmount,
                                 final double receiverAmount) {
        if (withdrawFunds(donorAmount)) {
            targetAccount.addFunds(receiverAmount);
            return true;
        }
        return false;
    }

    /**
     * Method to set output for non savings account
     * @param otherObjectMapper The ObjectMapper object to use for output
     * @param result The ObjectNode object to use for output
     * @param output The ArrayNode object to use for output
     * @param command The Command object to use for output
     */
    @Override
    public void setOutputNonSavingsAccount(final ObjectMapper otherObjectMapper,
                                           final ObjectNode result, final ArrayNode output,
                                           final Command command) {
        // Do nothing
    }
}
