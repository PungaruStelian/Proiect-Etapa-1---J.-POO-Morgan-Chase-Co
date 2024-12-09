package org.poo.solution;

import org.poo.utils.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.Locale;
import java.util.Objects;


// Singleton
public final class Handle {
    private static final Handle INSTANCE = new Handle();
    private final ObjectMapper objectMapper;

    /**
     * Constructor for Handle
     */
    private Handle() {
        objectMapper = new ObjectMapper();
    }

    /**
     * Method to get the INSTANCE of Handle
     * @return The iINSTANCE of Handle
     */
    public static Handle getInstance() {
        if (INSTANCE == null) {
            return new Handle();
        }
        return INSTANCE;
    }

    /**
     * Method to handle non-savings accounts
     * @param otherObjectMapper The ObjectMapper object
     * @param result The ObjectNode object
     * @param output The ArrayNode object
     * @param command The Command object
     */
    private void handleNonSavingsAccount(final ObjectMapper otherObjectMapper,
                                         final ObjectNode result, final ArrayNode output,
                                         final Command command) {
        ObjectNode outputNode = otherObjectMapper.createObjectNode();
        outputNode.put("description", "This is not a savings account");
        outputNode.put("timestamp", command.getTimestamp());
        addOutput(result, output, command, null, outputNode);
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
    private void addTransaction(final User user, final Command command, final String description,
                                final String account, final String cardHolder, final String card,
                                final String commerciant, final double amount,
                                final String currency , final String transferType,
                                final String senderIBAN, final String receiverIBAN,
                                final ArrayNode involvedAccounts) {
        ObjectNode transaction = objectMapper.createObjectNode();
        transaction.put("timestamp", command.getTimestamp());
        transaction.put("description", description);

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
     * Method to add the output
     * @param result The ObjectNode object
     * @param output The ArrayNode object
     * @param command The Command object
     * @param arrayNode The ArrayNode object
     * @param objectNode The ObjectNode object
     */
    private void addOutput(final ObjectNode result, final ArrayNode output, final Command command,
                           final ArrayNode arrayNode, final ObjectNode objectNode) {
        if (arrayNode != null) {
            result.set("output", arrayNode);
        } else {
            result.set("output", objectNode);
        }
        result.put("timestamp", command.getTimestamp());
        output.add(result);
    }

    /**
     * Method to create a transaction
     * @param object The Object object
     */
    public void createTransaction(final Object object) {
        for (User user : object.getUsers()) {
            user.setTransactions(objectMapper.createArrayNode());

        }
    }

    /**
     * Method to print the users
     * @param object The Object object
     * @param command The Command object
     * @param result The ObjectNode object
     * @param output The ArrayNode object
     */
    public void printUsers(final Object object, final Command command, final ObjectNode result,
                           final ArrayNode output) {
        ArrayNode usersArray = objectMapper.createArrayNode();
        for (User user : object.getUsers()) {
            ObjectNode userNode = objectMapper.valueToTree(user);
            usersArray.add(userNode);
        }
        addOutput(result, output, command, usersArray, null);
    }

    /**
     * Method to add an account
     * @param object The Object object
     * @param command The Command object
     */
    public void addAccount(final Object object, final Command command) {
        for (User user : object.getUsers()) {
            if (user.getEmail().equals(command.getEmail())) {
                Account newAccount = new Account.AccountBuilder()
                        .setIBAN(Utils.generateIBAN())
                        .setBalance(0.0)
                        .setCurrency(command.getCurrency())
                        .setType(command.getAccountType())
                        .setStatus("active")
                        .build();
                user.getAccounts().add(newAccount);
                addTransaction(user, command, "New account created",
                        newAccount.getIban(), null, null, null,
                        0, null, null, null, null, null);
            }
        }
    }

    /**
     * Method to delete an account
     * @param object The Object object
     * @param command The Command object
     * @param result The ObjectNode object
     * @param output The ArrayNode object
     */
    public void deleteAccount(final Object object, final Command command, final ObjectNode result,
                              final ArrayNode output) {
        for (User user : object.getUsers()) {
            List<Account> accounts = user.getAccounts();
            for (int i = 0; i < accounts.size(); i++) {
                Account account = accounts.get(i);
                if (account.getIban().equals(command.getAccount()) && account.getBalance() == 0) {
                    accounts.remove(i);
                    ObjectNode outputNode = objectMapper.createObjectNode();
                    outputNode.put("success", "Account deleted");
                    outputNode.put("timestamp", command.getTimestamp());
                    addOutput(result, output, command, null, outputNode);
                    return;
                }
            }
        }
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("error",
                "Account couldn't be deleted - see org.poo.transactions for details");
        outputNode.put("timestamp", command.getTimestamp());
        addOutput(result, output, command, null, outputNode);
    }

    /**
     * Method to create a card
     * @param object The Object object
     * @param command The Command object
     */
    public void createCard(final Object object, final Command command) {
        for (User user : object.getUsers()) {
            if (user.getEmail().equals(command.getEmail())) {
                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(command.getAccount())) {
                        PermanentCard newCard = new PermanentCard.PermanentCardBuilder()
                                .setCardNumber(Utils.generateCardNumber())
                                .setStatus("active")
                                .build();
                        account.getCards().add(newCard);
                        addTransaction(user, command, "New card created", account.getIban(),
                                user.getEmail(), newCard.getCardNumber(), null, 0,
                                null, null, null, null, null);
                    }
                }
            }
        }
    }

    /**
     * Method to add funds
     * @param object The Object object
     * @param command The Command object
     */
    public void addFunds(final Object object, final Command command) {
        for (User user : object.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(command.getAccount())) {
                    account.addFunds(command.getAmount());
                }
            }
        }
    }

    /**
     * Method to delete a card
     * @param object The Object object
     * @param command The Command object
     */
    public void deleteCard(final Object object, final Command command) {
        for (User user : object.getUsers()) {
            for (Account account : user.getAccounts()) {
                List<AbstractCard> cards = account.getCards();
                for (int i = 0; i < cards.size(); i++) {
                    AbstractCard currentCard = cards.get(i);
                    if (currentCard.getCardNumber().equals(command.getCardNumber())) {
                        cards.remove(i);
                        addTransaction(user, command, "The card has been destroyed",
                                account.getIban(), user.getEmail(), currentCard.getCardNumber(),
                                null, 0, null,null, null,
                                null, null);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Method to create a one-time card
     * @param object The Object object
     * @param command The Command object
     */
    public void createOneTimeCard(final Object object, final Command command) {
        for (User user : object.getUsers()) {
            if (user.getEmail().equals(command.getEmail())) {
                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(command.getAccount())) {
                        OneTimeCard newCard = new OneTimeCard.OneTimeCardBuilder()
                                .setUsed(false)
                                .setCardNumber(Utils.generateCardNumber())
                                .setStatus("active")
                                .build();
                        account.getCards().add(newCard);
                        addTransaction(user, command, "New card created", account.getIban(),
                                user.getEmail(), newCard.getCardNumber(), null, 0,
                                null, null, null, null, null);
                    }
                }
            }
        }
    }

    /**
     * Method to set the minimum balance
     * @param object The Object object
     * @param command The Command object
     */
    public void setMinimumBalance(final Object object, final Command command) {
        for (User user : object.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(command.getAccount())) {
                    account.setMinBalance(command.getMinBalance());
                    break;
                }
            }
        }
    }

    /**
     * Method to pay online
     * @param object The Object object
     * @param command The Command object
     * @param result The ObjectNode object
     * @param output The ArrayNode object
     */
    public void payOnline(final Object object, final Command command, final ObjectNode result,
                          final ArrayNode output) {
        for (User user : object.getUsers()) {
            for (Account account : user.getAccounts()) {
                for (AbstractCard card : account.getCards()) {
                    if (Objects.equals(card.getCardNumber(), command.getCardNumber())) {
                        if (!Objects.equals(command.getEmail(), user.getEmail())) {
                            ObjectNode outputNode = objectMapper.createObjectNode();
                            outputNode.put("description", "The card does not belong to the user");
                            outputNode.put("timestamp", command.getTimestamp());
                            addOutput(result, output, command, null, outputNode);
                            return;
                        }
                        if (card instanceof OneTimeCard && ((OneTimeCard) card).isUsed()) {
                            ObjectNode outputNode = objectMapper.createObjectNode();
                            outputNode.put("description", "Card already used");
                            outputNode.put("timestamp", command.getTimestamp());
                            addOutput(result, output, command, null, outputNode);
                            return;
                        }
                        if (card instanceof PermanentCard && ((PermanentCard) card).ruFrozen()) {
                            addTransaction(user, command, "The card is frozen", null, null,
                                    null, null, 0, null, null, null,
                                    null, null);
                            return;
                        }
                        double exhg;
                        if (!Objects.equals(command.getCurrency(), account.getCurrency())) {
                            exhg = account.getExchange(object, command.getCurrency(),
                                    account.getCurrency(), command.getAmount());
                        } else {
                            exhg = command.getAmount();
                        }
                        if (!account.withdrawFunds(exhg)) {
                            addTransaction(user, command, "Insufficient funds", null, null,
                                    null, null, 0, null, null, null,
                                    null, null);
                            return;
                        }
                        if (account.getBalance() <= account.getMinBalance()) {
                            if (card instanceof PermanentCard) {
                                ((PermanentCard) card).makeFrozen();
                            }
                            addTransaction(user, command,"You have reached the minimum amount "
                                            + "of funds, the card will be frozen", null, null,
                                    null, null, 0, null, null,
                                    null, null, null);
                            return;
                        }
                        if (account.getBalance() <= account.getMinBalance()
                                + Utils.WARNING_AMOUNT) {
                            if (card instanceof PermanentCard) {
                                ((PermanentCard) card).makeWarning();
                            }
                            addTransaction(user, command, "You have reached the minimum amount"
                                            + " of funds + 30, you will receive a warning", null,
                                    null, null, null, 0, null,
                                    null, null, null,null);
                            return;
                        }
                        addTransaction(user, command, "Card payment", account.getIban(), null,
                                null, command.getCommerciant(), exhg, null, null, null,
                                null, null);
                        if (card instanceof OneTimeCard) {
                            addTransaction(user, command, "The card has been destroyed",
                                    account.getIban(), user.getEmail(), card.getCardNumber(),
                                    null, 0, null, null, null,
                                    null,null);
                            ((OneTimeCard) card).markAsUsed();
                            addTransaction(user, command, "New card created", account.getIban(),
                                    user.getEmail(), card.getCardNumber(), null, 0, null,
                                    null, null, null, null);
                            return;
                        }
                        return;
                    }
                }
            }
        }
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("description", "Card not found");
        outputNode.put("timestamp", command.getTimestamp());
        addOutput(result, output, command, null, outputNode);
    }

    /**
     * Method to send money
     * @param object The Object object
     * @param command The Command object
     */
    public void sendMoney(final Object object, final Command command) {
        for (User user : object.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(command.getAccount())) {
                    for (User receiver : object.getUsers()) {
                        for (Account receiverAccount : receiver.getAccounts()) {
                            if (receiverAccount.getIban().equals(command.getReceiver())
                                    || (receiverAccount.getAlias() != null
                                    && receiverAccount.getAlias()
                                    .equals(command.getAccount()))) {
                                double exhg;
                                if (!Objects.equals(receiverAccount.getCurrency(),
                                        account.getCurrency())) {
                                    exhg = account.getExchange(object, account.getCurrency(),
                                            receiverAccount.getCurrency(), command.getAmount());
                                } else {
                                    exhg = command.getAmount();
                                }
                                if (!account.transferFunds(receiverAccount,
                                        command.getAmount(), exhg)) {
                                    addTransaction(user, command, "Insufficient funds", null,
                                            null, null, null, 0, null,
                                            null, null, null, null);
                                    return;
                                }

                                // Add the specified JSON object to the transactions
                                addTransaction(user, command, command.getDescription(),
                                        account.getIban(), null, null, null, command.getAmount(),
                                        account.getCurrency(), "sent", account.getIban(),
                                        receiverAccount.getIban(), null);

                                // Add the specified JSON object to the transactions
                                addTransaction(receiver, command, command.getDescription(),
                                        receiverAccount.getIban(), null, null, null,
                                        command.getAmount(), account.getCurrency(), "received",
                                        account.getIban(), receiverAccount.getIban(), null);
                                return;
                            }
                        }
                    }
                    return;
                }
            }
        }
    }

    /**
     * Method to set an alias
     * @param object The Object object
     * @param command The Command object
     */
    public void setAlias(final Object object, final Command command) {
        for (User user : object.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(command.getAccount())) {
                    account.setAlias(command.getAlias());
                    return;
                }
            }
        }
    }

    /**
     * Method to print transactions
     * @param object The Object object
     * @param command The Command object
     * @param result The ObjectNode object
     * @param output The ArrayNode object
     */
    public void printTransactions(final Object object, final Command command,
                                  final ObjectNode result, final ArrayNode output) {
        for (User user : object.getUsers()) {
            if (user.getEmail().equals(command.getEmail())) {
                ArrayNode filteredTransactions = objectMapper.createArrayNode();
                for (int i = 0; i < user.getTransactions().size(); i++) {
                    ObjectNode transaction = (ObjectNode) user.getTransactions().get(i);
                    if (transaction.get("timestamp").asInt() < command.getTimestamp()) {
                        // Create a copy of the transaction
                        ObjectNode transactionCopy = transaction.deepCopy();
                        if (!transaction.get("description").asText().equals("New card created")
                                && !transaction.get("description").asText().
                                equals("The card has been destroyed")) {
                            transactionCopy.remove("account");
                        }
                        filteredTransactions.add(transactionCopy);
                    }
                }
                addOutput(result, output, command, filteredTransactions, null);
                return;
            }
        }
    }

    /**
     * Method to check the card status
     * @param object The Object object
     * @param command The Command object
     * @param result The ObjectNode object
     * @param output The ArrayNode object
     */
    public void checkCardStatus(final Object object, final Command command,
                                final ObjectNode result, final ArrayNode output) {
        for (User user : object.getUsers()) {
            for (Account account : user.getAccounts()) {
                for (AbstractCard card : account.getCards()) {
                    if (card.getCardNumber().equals(command.getCardNumber())) {
                        if (account.getBalance() <= account.getMinBalance()) {
                            if (card instanceof PermanentCard) {
                                ((PermanentCard) card).makeFrozen();
                            }
                            addTransaction(user, command, "You have reached the minimum amount "
                                            + "of funds, the card will be frozen", null, null,
                                    null, null, 0, null, null,
                                    null, null, null);
                            return;
                        }
                        return;
                    }
                }
            }
        }
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("description", "Card not found");
        outputNode.put("timestamp", command.getTimestamp());
        addOutput(result, output, command, null, outputNode);
    }

    /**
     * Method to split a payment
     * @param object The Object object
     * @param command The Command object
     */
    public void splitPayment(final Object object, final Command command) {
        for (User user : object.getUsers()) {
            for (Account account : user.getAccounts()) {
                for (String iban : command.getAccounts()) {
                    if (account.getIban().equals(iban)) {
                        double exhg;
                        if (!Objects.equals(account.getCurrency(), command.getCurrency())) {
                            exhg = account.getExchange(object, command.getCurrency(),
                                    account.getCurrency(), command.getAmount()
                                            / command.getAccounts().size());
                        } else {
                            exhg = command.getAmount() / command.getAccounts().size();
                        }
                        if (!account.withdrawFunds(exhg)) {
                            addTransaction(user, command, "Insufficient funds", null, null,
                                    null, null, 0, null, null,
                                    null, null, null);
                            return;
                        }
                        ArrayNode involvedAccounts = objectMapper.createArrayNode();
                        for (String involvedIban : command.getAccounts()) {
                            involvedAccounts.add(involvedIban);
                        }
                        addTransaction(user, command, "Split payment of "
                                + String.format(Locale.US, "%.2f", command.getAmount()) + " "
                                + command.getCurrency(), null, null, null, null,
                                command.getAmount() / command.getAccounts().size(),
                                command.getCurrency(), null, null, null, involvedAccounts);
                    }
                }
            }
        }
    }

    /**
     * Method to change the interest rate
     * @param object The Object object
     * @param command The Command object
     * @param result The ObjectNode object
     * @param output The ArrayNode object
     */
    public void changeInterestRate(final Object object, final Command command,
                                   final ObjectNode result, final ArrayNode output) {
        for (User user : object.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(command.getAccount())) {
                    if (!Objects.equals(account.getType(), "savings")) {
                        handleNonSavingsAccount(objectMapper, result, output, command);
                    }
                    account.setInterestRate(command.getInterestRate());
                    addTransaction(user, command, "Interest rate of the account changed to "
                            + command.getInterestRate(), null, null, null, null,
                            0, null, null, null, null, null);
                    return;
                }
            }
        }
    }

    /**
     * Method to add interest
     * @param object The Object object
     * @param command The Command object
     * @param result The ObjectNode object
     * @param output The ArrayNode object
     */
    public void addInterest(final Object object, final Command command, final ObjectNode result,
                            final ArrayNode output) {
        for (User user : object.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(command.getAccount())) {
                    if (!Objects.equals(account.getType(), "savings")) {
                        handleNonSavingsAccount(objectMapper, result, output, command);
                    }
                    double interest = account.getBalance() * account.getInterestRate()
                            / Utils.MAXIMUM_PERCENTAGE;
                    account.addFunds(interest);
                    addTransaction(user, command, "Interest added", null, null, null,
                            null, interest, null, null, null, null,
                            null);
                    return;
                }
            }
        }
    }

    /**
     * Method to report
     * @param object The Object object
     * @param command The Command object
     * @param result The ObjectNode object
     * @param output The ArrayNode object
     */
    public void report(final Object object, final Command command, final ObjectNode result,
                       final ArrayNode output) {
        for (User user : object.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(command.getAccount())) {
                    ArrayNode filteredTransactions = objectMapper.createArrayNode();
                    for (int i = 0; i < user.getTransactions().size(); i++) {
                        ObjectNode transaction = (ObjectNode) user.getTransactions().get(i);
                        ObjectNode transactionCopy = transaction.deepCopy();
                        if (!transaction.get("description").asText().equals("New card created")
                                && !transaction.get("description").asText().equals("The card "
                                + "has been destroyed")) {
                            transactionCopy.remove("account");
                        }

                        int timestamp = transaction.get("timestamp").asInt();
                        if (timestamp >= command.getStartTimestamp() && timestamp
                                <= command.getEndTimestamp()) {
                            filteredTransactions.add(transactionCopy);
                        }
                    }
                    ObjectNode outputNode = objectMapper.createObjectNode();
                    outputNode.put("IBAN", account.getIban());
                    outputNode.put("balance", account.getBalance());
                    outputNode.put("currency", account.getCurrency());
                    outputNode.set("transactions", filteredTransactions);
                    addOutput(result, output, command, null, outputNode);
                    return;
                }
            }
        }
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("description", "Account not found");
        outputNode.put("timestamp", command.getTimestamp());
        addOutput(result, output, command, null, outputNode);
    }

    /**
     * Method to spendings report
     * @param object The Object object
     * @param command The Command object
     * @param result The ObjectNode object
     * @param output The ArrayNode object
     */
    public void spendingsReport(final Object object, final Command command,
                                final ObjectNode result, final ArrayNode output) {
        for (User user : object.getUsers()) {
            for (int a = 0; a < user.getAccounts().size(); a++) {
                Account account = user.getAccounts().get(a);
                if (account.getIban().equals(command.getAccount())) {
                    ArrayNode filteredTransactions = objectMapper.createArrayNode();
                    String[] commerciants = new String[user.getTransactions().size()];
                    double[] totals = new double[user.getTransactions().size()];
                    int commerciantCount = 0;
                    for (int i = 0; i < user.getTransactions().size(); i++) {
                        ObjectNode transaction = (ObjectNode) user.getTransactions().get(i);
                        int timestamp = transaction.get("timestamp").asInt();
                        if (timestamp >= command.getStartTimestamp()
                                && timestamp <= command.getEndTimestamp()
                                && transaction.has("commerciant")
                                && transaction.get("account").asText().
                                equals(command.getAccount())) {
                            ObjectNode transactionCopy = transaction.deepCopy();
                            transactionCopy.remove("account");
                            filteredTransactions.add(transactionCopy);
                            String currentCommerciant = transaction.get("commerciant").asText();
                            double amount = transaction.get("amount").asDouble();
                            int existingIndex = -1;
                            for (int j = 0; j < commerciantCount; j++) {
                                if (commerciants[j].equals(currentCommerciant)) {
                                    existingIndex = j;
                                    break;
                                }
                            }
                            if (existingIndex != -1) {
                                totals[existingIndex] += amount;
                            } else {
                                commerciants[commerciantCount] = currentCommerciant;
                                totals[commerciantCount] = amount;
                                commerciantCount++;
                            }
                        }
                    }
                    for (int i = 0; i < commerciantCount - 1; i++) {
                        for (int j = 0; j < commerciantCount - i - 1; j++) {
                            if (totals[j] < totals[j + 1]) {
                                String tempCommerciant = commerciants[j];
                                commerciants[j] = commerciants[j + 1];
                                commerciants[j + 1] = tempCommerciant;
                                double tempTotal = totals[j];
                                totals[j] = totals[j + 1];
                                totals[j + 1] = tempTotal;
                            }
                        }
                    }
                    ArrayNode commerciantsArray = objectMapper.createArrayNode();
                    for (int i = 0; i < commerciantCount; i++) {
                        ObjectNode commerciantNode = objectMapper.createObjectNode();
                        commerciantNode.put("commerciant", commerciants[i]);
                        commerciantNode.put("total", totals[i]);
                        commerciantsArray.add(commerciantNode);
                    }
                    ObjectNode outputNode = objectMapper.createObjectNode();
                    outputNode.put("IBAN", account.getIban());
                    outputNode.put("balance", account.getBalance());
                    outputNode.put("currency", account.getCurrency());
                    outputNode.set("transactions", filteredTransactions);
                    outputNode.set("commerciants", commerciantsArray);
                    addOutput(result, output, command, null, outputNode);
                    return;
                }
            }
        }
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("description", "Account not found");
        outputNode.put("timestamp", command.getTimestamp());
        addOutput(result, output, command, null, outputNode);
    }
}
