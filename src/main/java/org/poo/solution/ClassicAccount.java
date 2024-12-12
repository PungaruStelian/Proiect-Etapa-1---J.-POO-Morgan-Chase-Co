package org.poo.solution;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.poo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClassicAccount implements AccountType {
    @JsonProperty("IBAN")
    private String iban;
    private String currency;
    @JsonIgnore
    private String alias;
    private double balance;
    @JsonIgnore
    private double minBalance;
    private List<AbstractCard> cards;
    @JsonIgnore
    private double interestRate;

    public ClassicAccount() {
        this.cards = new ArrayList<>();
    }

    public static class ClassicAccountBuilder {
        private String iban;
        private String currency;
        private double balance;
        private String alias;

        /**
         * Method to set the alias of the account
         * @param otherAlias The alias of the account
         * @return The ClassicAccountBuilder object
         */
        public ClassicAccountBuilder setAlias(final String otherAlias) {
            this.alias = otherAlias;
            return this;
        }

        /**
         * Method to set the IBAN of the account
         * @param otherIban The IBAN of the account
         * @return The ClassicAccountBuilder object
         */
        public ClassicAccountBuilder setIban(final String otherIban) {
            this.iban = otherIban;
            return this;
        }

        /**
         * Method to set the currency of the account
         * @param otherCurrency The currency of the account
         * @return The ClassicAccountBuilder object
         */
        public ClassicAccountBuilder setCurrency(final String otherCurrency) {
            this.currency = otherCurrency;
            return this;
        }

        /**
         * Method to set the balance of the account
         * @param otherBalance The balance of the account
         * @return The ClassicAccountBuilder object
         */
        public ClassicAccountBuilder setBalance(final double otherBalance) {
            this.balance = otherBalance;
            return this;
        }

        /**
         * Method to build the ClassicAccount object
         * @return The ClassicAccount object
         */
        public ClassicAccount build() {
            ClassicAccount classicAccount = new ClassicAccount();
            classicAccount.iban = this.iban;
            classicAccount.currency = this.currency;
            classicAccount.balance = this.balance;
            classicAccount.alias = this.alias;
            return classicAccount;
        }
    }

    /**
     * Method set minimum balance
     * @param otherMinBalance The minimum balance
     */
    @Override
    public void setMinBalance(final double otherMinBalance) {
        this.minBalance = otherMinBalance;
    }

    /**
     * Method to get the currency of the account
     * @return The currency of the account
     */
    @Override
    public String getType() {
        return "classic";
    }

    /**
     * Method to get the currency of the account
     * @return The currency of the account
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
        ObjectNode outputNode = otherObjectMapper.createObjectNode();
        outputNode.put("description", "This is not a savings account");
        outputNode.put("timestamp", command.getTimestamp());
        Utils.addOutput(result, output, command, null, outputNode);
    }
}
