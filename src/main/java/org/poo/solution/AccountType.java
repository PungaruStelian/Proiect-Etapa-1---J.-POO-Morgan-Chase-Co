package org.poo.solution;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public interface AccountType {
    /**
     * Method to get the type of account
     * @return The type of account
     */
    String getType();

    /**
     * Method to get the IBAN of the account
     * @return The IBAN of the account
     */
    String getIban();

    /**
     * Method to get the currency of the account
     * @return The currency of the account
     */
    String getCurrency();

    /**
     * Method to get the cards of the account
     * @return The cards of the account
     */
    List<AbstractCard> getCards();

    /**
     * Method to set the minimum balance of the account
     * @param minBalance The minimum balance of the account
     */
    void setMinBalance(double minBalance);

    /**
     * Method to get the minimum balance of the account
     * @return The minimum balance of the account
     */
    double getMinBalance();

    /**
     * Method to get the balance of the account
     * @return The balance of the account
     */
    double getBalance();

    /**
     * Method to get the exchange rate
     * @param object The object to get the exchange rate from
     * @param from The currency to exchange from
     * @param to The currency to exchange to
     * @param amount The amount to exchange
     * @return The exchange rate
     */
    double getExchange(Object object, String from, String to,
                       double amount);

    /**
     * Method to get the alias of the account
     * @return The alias of the account
     */
    String getAlias();

    /**
     * Method to set the alias of the account
     * @param alias The alias of the account
     */
    void setAlias(String alias);

    /**
     * Method to add funds to the account
     * @param amount The amount to add
     */
    void addFunds(double amount);

    /**
     * Method to withdraw funds from the account
     * @param amount The amount to withdraw
     * @return True if the funds were withdrawn, false otherwise
     */
    boolean withdrawFunds(double amount);

    /**
     * Method to transfer funds between accounts
     * @param targetAccount The target account
     * @param donorAmount The amount to transfer from the donor account
     * @param receiverAmount The amount to transfer to the receiver account
     * @return True if the funds were transferred, false otherwise
     */
    boolean transferFunds(AccountType targetAccount, double donorAmount, double receiverAmount);

    /**
     * Method to apply interest to the account
     */
    void setOutputNonSavingsAccount(ObjectMapper objectMapper, ObjectNode result, ArrayNode output,
                                    Command command);

    /**
     * Method to set the interest rate of the account
     * @param interestRate The interest rate of the account
     */
    void setInterestRate(double interestRate);

    /**
     * Method to get the interest rate of the account
     * @return The interest rate of the account
     */
    double getInterestRate();
}
