package org.poo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.solution.Command;
import org.poo.solution.User;

import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public final class Utils {
    private Utils() {
        // Checkstyle error free constructor
    }

    public static final int WARNING_AMOUNT = 30;
    public static final int MAXIMUM_PERCENTAGE = 100;

    private static final int IBAN_SEED = 1;
    private static final int CARD_SEED = 2;
    private static final int DIGIT_BOUND = 10;
    private static final int DIGIT_GENERATION = 16;
    private static final String RO_STR = "RO";
    private static final String POO_STR = "POOB";
    private static Random ibanRandom = new Random(IBAN_SEED);
    private static Random cardRandom = new Random(CARD_SEED);

    /**
     * Utility method for generating an IBAN code.
     *
     * @return the IBAN as String
     */
    public static String generateIBAN() {
        StringBuilder sb = new StringBuilder(RO_STR);
        for (int i = 0; i < RO_STR.length(); i++) {
            sb.append(ibanRandom.nextInt(DIGIT_BOUND));
        }

        sb.append(POO_STR);
        for (int i = 0; i < DIGIT_GENERATION; i++) {
            sb.append(ibanRandom.nextInt(DIGIT_BOUND));
        }

        return sb.toString();
    }

    /**
     * Utility method for generating a card number.
     *
     * @return the card number as String
     */
    public static String generateCardNumber() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < DIGIT_GENERATION; i++) {
            sb.append(cardRandom.nextInt(DIGIT_BOUND));
        }

        return sb.toString();
    }

    /**
     * Resets the seeds between runs.
     */
    public static void resetRandom() {
        ibanRandom = new Random(IBAN_SEED);
        cardRandom = new Random(CARD_SEED);
    }

    /**
     * Method to add a transaction
     * @param user The User object
     * @param command The Command object
     * @param description The description of the transaction
     * @param account The account of the transaction
     * @param cardHolder The card holder of the transaction
     * @param card The card of the transaction
     * @param commerciant The commerciant of the transaction
     * @param amount The amount of the transaction
     * @param currency The currency of the transaction
     * @param transferType The transfer type of the transaction
     * @param senderIBAN The sender IBAN of the transaction
     * @param receiverIBAN The receiver IBAN of the transaction
     * @param involvedAccounts The involved accounts of the transaction
     */
    public static void addTransaction(final ObjectMapper objectMapper, final User user,
                               final Command command, final String description,
                               final String account, final String cardHolder, final String card,
                               final String commerciant, final double amount,
                               final String currency, final String transferType,
                               final String senderIBAN, final String receiverIBAN,
                               final ArrayNode involvedAccounts, final String error) {
        ObjectNode transaction = objectMapper.createObjectNode();
        transaction.put("timestamp", command.getTimestamp());
        transaction.put("description", description);

        if (error != null) {
            transaction.put("error", error);
        }

        if (account != null) {
            transaction.put("account", account);
        }

        if (cardHolder != null) {
            transaction.put("cardHolder", cardHolder);
        }

        if (card != null) {
            transaction.put("card", card);
        }

        if (commerciant != null) {
            transaction.put("commerciant", commerciant);
        }

        if (Objects.equals(command.getCommand(), "splitPayment")) {
            transaction.put("amount", amount);
            transaction.put("currency", currency);
        } else {

            if (amount != 0 && currency != null) {
                transaction.put("amount", amount + " " + currency);
            }

            if (amount != 0 && currency == null) {
                transaction.put("amount", amount);
            }
        }

        if (transferType != null) {
            transaction.put("transferType", transferType);
        }

        if (senderIBAN != null) {
            transaction.put("senderIBAN", senderIBAN);
        }

        if (receiverIBAN != null) {
            transaction.put("receiverIBAN", receiverIBAN);
        }

        if (involvedAccounts != null) {
            transaction.set("involvedAccounts", involvedAccounts);
        }

        user.getTransactions().add(transaction);
    }

    /**
     * Method to add a transaction for new account
     * @param user The User object
     * @param command The Command object
     * @param account The account of the transaction
     */
    public static void addTransactionForNewAccount(final ObjectMapper objectmapper,
                                                   final User user, final Command command,
                                                   final String account) {
        addTransaction(objectmapper, user, command, "New account created", account,
                null, null, null, 0, null, null,
                null, null, null, null);
    }

    /**
     * Method to add a transaction for insufficient funds
     * @param user The User object
     * @param command The Command object
     */
    public static void addTransactionForInsufficientFunds(final ObjectMapper objectMapper,
                                                          final User user, final Command command) {
        addTransaction(objectMapper, user, command, "Insufficient funds", null, null, null,
                null, 0, null, null, null, null,
                null, null);
    }

    /**
     * Method to add a transaction for card payment
     * @param user The User object
     * @param command The Command object
     * @param account The account of the transaction
     * @param commerciant The commerciant of the transaction
     * @param amount The amount of the transaction
     */
    public static void addTransactionForCardPayment(final ObjectMapper objectMapper,
                                                    final User user, final Command command,
                                                    final String account, final String commerciant,
                                                    final double amount) {
        addTransaction(objectMapper, user, command, "Card payment", account, null,
                null, commerciant, amount, null, null, null,
                null, null, null);
    }

    /**
     * Method to add a transaction for card destruction
     * @param user The User object
     * @param command The Command object
     * @param account The account of the transaction
     * @param email The email of the transaction
     * @param cardNumber The card number of the transaction
     */
    public static void addTransactionForCardDestruction(final ObjectMapper objectMapper,
                                                        final User user, final Command command,
                                                        final String account, final String email,
                                                        final String cardNumber) {
        addTransaction(objectMapper, user, command, "The card has been destroyed", account, email,
                cardNumber, null, 0, null, null, null,
                null, null, null);
    }

    /**
     * Method to add a transaction for new card
     * @param user The User object
     * @param command The Command object
     * @param account The account of the transaction
     * @param email The email of the transaction
     * @param cardNumber The card number of the transaction
     */
    public static void addTransactionForNewCard(final ObjectMapper objectMapper, final User user,
                                                final Command command, final String account,
                                                final String email, final String cardNumber) {
        addTransaction(objectMapper, user, command, "New card created", account, email, cardNumber,
                null, 0, null, null, null, null,
                null, null);
    }

    /**
     * Method to add a transaction for warning
     * @param user The User object
     * @param command The Command object
     * @param description The description of the transaction
     * @param account The account of the transaction
     */
    public static void addTransactionForWarning(final ObjectMapper objectMapper, final User user,
                                                final Command command, final String description,
                                                final String account) {
        addTransaction(objectMapper, user, command, description, account, null, null, null, 0,
                null, null, null, null, null, null);
    }

    /**
     * Method to add a transaction for freezing card
     * @param user The User object
     * @param command The Command object
     * @param description The description of the transaction
     * @param account The account of the transaction
     */
    public static void addTransactionForFreezingCard(final ObjectMapper objectMapper,
                                                     final User user, final Command command,
                                                     final String description,
                                                     final String account) {
        addTransaction(objectMapper, user, command, description, account, null, null,
                null, 0, null, null, null, null,
                null, null);
    }

    /**
     * Method to add a transaction for changing interest rate
     * @param user The User object
     * @param command The Command object
     */
    public static void addTransactionForChangingInterestRate(final ObjectMapper objectMapper,
                                                             final User user,
                                                             final Command command) {
        addTransaction(objectMapper, user, command, "Interest rate of the account changed to "
                        + command.getInterestRate(), null, null, null, null,
                0, null, null, null, null, null, null);
    }

    /**
     * Method to add a transaction for adding interest
     * @param user The User object
     * @param command The Command object
     * @param interestAmount The interest amount of the transaction
     */
    public static void addTransactionForAddingInterest(final ObjectMapper objectMapper,
                                                       final User user, final Command command,
                                                       final double interestAmount) {
        addTransaction(objectMapper, user, command, "Interest added", null, null, null,
                null, interestAmount, null, null, null, null,
                null, null);
    }

    /**
     * Method to add a transaction for sending money
     * @param user The User object
     * @param command The Command object
     * @param account The account of the transaction
     * @param senderIBAN The sender IBAN of the transaction
     * @param receiverIBAN The receiver IBAN of the transaction
     * @param amount The amount of the transaction
     * @param currency The currency of the transaction
     */
    public static void addTransactionForSendingMoney(final ObjectMapper objectMapper,
                                                     final User user, final Command command,
                                                     final String account, final String senderIBAN,
                                                     final String receiverIBAN, final double amount,
                                                     final String currency) {
        addTransaction(objectMapper, user, command, command.getDescription(), account,
                null, null, null, amount, currency, "sent",
                senderIBAN, receiverIBAN, null, null);
    }

    /**
     * Method to add a transaction for receiving money
     * @param user The User object
     * @param command The Command object
     * @param account The account of the transaction
     * @param senderIBAN The sender IBAN of the transaction
     * @param receiverIBAN The receiver IBAN of the transaction
     * @param amount The amount of the transaction
     * @param currency The currency of the transaction
     */
    public static void addTransactionForReceivingMoney(final ObjectMapper objectMapper,
                                                       final User user, final Command command,
                                                       final String account,
                                                       final String senderIBAN,
                                                       final String receiverIBAN,
                                                       final double amount,
                                                       final String currency) {
        addTransaction(objectMapper, user, command, command.getDescription(), account,
                null, null, null, amount, currency, "received",
                senderIBAN, receiverIBAN, null, null);
    }

    /**
     * Method to add a transaction for split payment
     * @param user The User object
     * @param command The Command object
     * @param involvedAccounts The involved accounts of the transaction
     */
    public static void addTransactionForSplitPayment(final ObjectMapper objectMapper,
                                                     final User user, final Command command,
                                                     final ArrayNode involvedAccounts) {
        addTransaction(objectMapper, user, command, "Split payment of "
                        + String.format(Locale.US, "%.2f", command.getAmount())
                        + " " + command.getCurrency(), null, null, null,
                null, command.getAmount() / command.getAccounts().size(),
                command.getCurrency(), null, null, null,
                involvedAccounts, null);
    }

    /**
     * Method to add a transaction for new account with an error message
     * @param user The User object
     * @param command The Command object
     * @param errorMessage The error message of the transaction
     */
    public static void addTransactionForSplitPaymentWithError(final ObjectMapper objectMapper,
                                                              final User user,
                                                              final Command command,
                                                              final ArrayNode involvedAccounts,
                                                              final String errorMessage) {
        addTransaction(objectMapper, user, command, "Split payment of "
                        + String.format(Locale.US, "%.2f", command.getAmount())
                        + " " + command.getCurrency(), null, null, null,
                null, command.getAmount() / command.getAccounts().size(),
                command.getCurrency(), null, null, null,
                involvedAccounts, errorMessage);
    }

    /**
     * Method to add the output
     * @param result The ObjectNode object
     * @param output The ArrayNode object
     * @param command The Command object
     * @param arrayNode The ArrayNode object
     * @param objectNode The ObjectNode object
     */
    public static void addOutput(final ObjectNode result, final ArrayNode output,
                                 final Command command, final ArrayNode arrayNode,
                                 final ObjectNode objectNode) {
        if (arrayNode != null) {
            result.set("output", arrayNode);
        } else {
            result.set("output", objectNode);
        }
        result.put("timestamp", command.getTimestamp());
        output.add(result);
    }
}
