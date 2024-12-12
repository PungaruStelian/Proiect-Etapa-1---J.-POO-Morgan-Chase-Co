package org.poo.solution;

import org.poo.utils.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;
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
        Utils.addOutput(result, output, command, usersArray, null);
    }

    /**
     * Method to add an account
     * @param object The Object object
     * @param command The Command object
     */
    public void addAccount(final Object object, final Command command) {
        for (User user : object.getUsers()) {
            if (user.getEmail().equals(command.getEmail())) {
                AccountType newAccount;
                if ("classic".equalsIgnoreCase(command.getAccountType())) {
                    newAccount = new ClassicAccount.ClassicAccountBuilder()
                            .setIban(Utils.generateIBAN())
                            .setCurrency(command.getCurrency())
                            .setBalance(0.0)
                            .build();
                } else {
                    newAccount = new SavingsAccount.SavingsAccountBuilder()
                            .setIban(Utils.generateIBAN())
                            .setCurrency(command.getCurrency())
                            .setBalance(0.0)
                            .build();
                }
                user.getAccounts().add(newAccount);
                Utils.addTransactionForNewAccount(objectMapper, user,
                        command, newAccount.getIban());
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
            List<AccountType> accounts = user.getAccounts();
            for (int i = 0; i < accounts.size(); i++) {
                AccountType account = accounts.get(i);
                if (account.getIban().equals(command.getAccount())) {
                    if (account.getBalance() > 0) {
                        ObjectNode outputNode = objectMapper.createObjectNode();
                        outputNode.put("description",
                                "Account couldn't be deleted - there are funds remaining");
                        outputNode.put("timestamp", command.getTimestamp());
                        user.getTransactions().add(outputNode);

                        ObjectNode outputs = objectMapper.createObjectNode();
                        outputs.put("error",
                                "Account couldn't be deleted - see "
                                        + "org.poo.transactions for details");
                        outputs.put("timestamp", command.getTimestamp());
                        Utils.addOutput(result, output, command, null, outputs);
                    } else {
                        accounts.remove(i);
                        ObjectNode outputNode = objectMapper.createObjectNode();
                        outputNode.put("success", "Account deleted");
                        outputNode.put("timestamp", command.getTimestamp());
                        Utils.addOutput(result, output, command, null, outputNode);
                    }
                    return;
                }
            }
        }
    }

    /**
     * Method to create a card
     * @param object The Object object
     * @param command The Command object
     */
    public void createCard(final Object object, final Command command) {
        for (User user : object.getUsers()) {
            if (user.getEmail().equals(command.getEmail())) {
                for (AccountType account : user.getAccounts()) {
                    if (account.getIban().equals(command.getAccount())) {
                        PermanentCard newCard = new PermanentCard.PermanentCardBuilder()
                                .setCardNumber(Utils.generateCardNumber())
                                .setStatus("active")
                                .build();
                        account.getCards().add(newCard);
                        Utils.addTransactionForNewCard(objectMapper, user, command,
                                account.getIban(), user.getEmail(), newCard.getCardNumber());
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
            for (AccountType account : user.getAccounts()) {
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
            for (AccountType account : user.getAccounts()) {
                List<AbstractCard> cards = account.getCards();
                for (int i = 0; i < cards.size(); i++) {
                    AbstractCard currentCard = cards.get(i);
                    if (currentCard.getCardNumber().equals(command.getCardNumber())) {
                        cards.remove(i);
                        Utils.addTransactionForCardDestruction(objectMapper, user, command,
                                account.getIban(), user.getEmail(), currentCard.getCardNumber());
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
                for (AccountType account : user.getAccounts()) {
                    if (account.getIban().equals(command.getAccount())) {
                        OneTimeCard newCard = new OneTimeCard.OneTimeCardBuilder()
                                .setUsed(false)
                                .setCardNumber(Utils.generateCardNumber())
                                .setStatus("active")
                                .build();
                        account.getCards().add(newCard);
                        Utils.addTransactionForNewCard(objectMapper, user, command,
                                account.getIban(), user.getEmail(), newCard.getCardNumber());
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
            for (AccountType account : user.getAccounts()) {
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
            for (AccountType account : user.getAccounts()) {
                for (AbstractCard card : account.getCards()) {
                    if (Objects.equals(card.getCardNumber(), command.getCardNumber())) {
                        if (!Objects.equals(command.getEmail(), user.getEmail())) {
                            ObjectNode outputNode = objectMapper.createObjectNode();
                            outputNode.put("description", "The card does not belong to the user");
                            outputNode.put("timestamp", command.getTimestamp());
                            Utils.addOutput(result, output, command, null, outputNode);
                            return;
                        }
                        if (card.ruUsed()) {
                            ObjectNode outputNode = objectMapper.createObjectNode();
                            outputNode.put("description", "Card already used");
                            outputNode.put("timestamp", command.getTimestamp());
                            Utils.addOutput(result, output, command, null, outputNode);
                            return;
                        }
                        if (card.ruFrozen()) {
                            Utils.addTransactionForFreezingCard(objectMapper, user, command,
                                    "The card is frozen", account.getIban());
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
                            Utils.addTransactionForInsufficientFunds(objectMapper, user, command);
                            return;
                        }
                        if (account.getBalance() <= account.getMinBalance()) {
                            card.makeFrozen();
                            Utils.addTransactionForFreezingCard(objectMapper, user, command,
                                    "You have reached the minimum amount "
                                    + "of funds, the card will be frozen", account.getIban());
                        } else if (account.getBalance() <= account.getMinBalance()
                                + Utils.WARNING_AMOUNT) {
                            card.makeWarning();
                            Utils.addTransactionForWarning(objectMapper, user, command,
                                    "You have reached the minimum amount "
                                    + "of funds, the card will be frozen", account.getIban());
                        }
                        Utils.addTransactionForCardPayment(objectMapper, user, command,
                                account.getIban(), command.getCommerciant(), exhg);
                        card.handleCardDestruction(objectMapper, user, command, account, this);
                        return;
                    }
                }
            }
        }
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("description", "Card not found");
        outputNode.put("timestamp", command.getTimestamp());
        Utils.addOutput(result, output, command, null, outputNode);
    }

    /**
     * Method to send money
     * @param object The Object object
     * @param command The Command object
     */
    public void sendMoney(final Object object, final Command command) {
        for (User user : object.getUsers()) {
            for (AccountType account : user.getAccounts()) {
                if (account.getIban().equals(command.getAccount())) {
                    for (User receiver : object.getUsers()) {
                        for (AccountType receiverAccount : receiver.getAccounts()) {
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
                                    Utils.addTransactionForInsufficientFunds(objectMapper,
                                            user, command);
                                    return;
                                }
                                Utils.addTransactionForSendingMoney(objectMapper, user, command,
                                        account.getIban(), account.getIban(),
                                        receiverAccount.getIban(), command.getAmount(),
                                        account.getCurrency());

                                // Add the specified JSON object to the transactions
                                Utils.addTransactionForReceivingMoney(objectMapper, receiver,
                                        command, receiverAccount.getIban(), account.getIban(),
                                        receiverAccount.getIban(), exhg,
                                        receiverAccount.getCurrency());
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
            for (AccountType account : user.getAccounts()) {
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
                Utils.addOutput(result, output, command, filteredTransactions, null);
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
            for (AccountType account : user.getAccounts()) {
                for (AbstractCard card : account.getCards()) {
                    if (card.getCardNumber().equals(command.getCardNumber())) {
                        if (account.getBalance() <= account.getMinBalance()) {
                            card.makeFrozen();
                            Utils.addTransactionForWarning(objectMapper, user, command,
                                    "You have reached the minimum amount "
                                    + "of funds, the card will be frozen", account.getIban());
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
        Utils.addOutput(result, output, command, null, outputNode);
    }

    /**
     * Method to split a payment
     * @param object The Object object
     * @param command The Command object
     */
    public void splitPayment(final Object object, final Command command) {
        List<String> poorIbans = new ArrayList<>();
        for (String iban : command.getAccounts()) {
            for (User user : object.getUsers()) {
                for (AccountType account : user.getAccounts()) {
                    if (account.getIban().equals(iban)) {
                        double exhg;
                        if (!Objects.equals(account.getCurrency(), command.getCurrency())) {
                            exhg = account.getExchange(object, command.getCurrency(),
                                    account.getCurrency(), command.getAmount()
                                            / command.getAccounts().size());
                        } else {
                            exhg = command.getAmount() / command.getAccounts().size();
                        }
                        if (account.getBalance() < exhg) {
                            poorIbans.add(account.getIban());
                        }
                    }
                }
            }
        }
        if (poorIbans.isEmpty()) {
            for (User user : object.getUsers()) {
                for (AccountType account : user.getAccounts()) {
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
                            account.withdrawFunds(exhg);
                            ArrayNode involvedAccounts = objectMapper.createArrayNode();
                            for (String involvedIban : command.getAccounts()) {
                                involvedAccounts.add(involvedIban);
                            }
                            Utils.addTransactionForSplitPayment(objectMapper, user, command,
                                    involvedAccounts);
                        }
                    }
                }
            }
        } else {
            for (User user : object.getUsers()) {
                for (AccountType account : user.getAccounts()) {
                    for (String iban : command.getAccounts()) {
                        if (account.getIban().equals(iban)) {
                            ArrayNode involvedAccounts = objectMapper.createArrayNode();
                            for (String involvedIban : command.getAccounts()) {
                                involvedAccounts.add(involvedIban);
                            }
                            Utils.addTransactionForSplitPaymentWithError(objectMapper, user,
                                    command, involvedAccounts, "Account "
                                            + poorIbans.getLast()
                                            + " has insufficient funds for a split payment.");
                        }
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
            for (AccountType account : user.getAccounts()) {
                if (account.getIban().equals(command.getAccount())) {
                    if (!Objects.equals(account.getType(), "savings")) {
                        account.setOutputNonSavingsAccount(objectMapper, result, output, command);
                    }
                    account.setInterestRate(command.getInterestRate());
                    Utils.addTransactionForChangingInterestRate(objectMapper, user, command);
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
            for (AccountType account : user.getAccounts()) {
                if (account.getIban().equals(command.getAccount())) {
                    if (!Objects.equals(account.getType(), "savings")) {
                        account.setOutputNonSavingsAccount(objectMapper, result, output, command);
                    }
                    double interest = account.getBalance() * account.getInterestRate()
                            / Utils.MAXIMUM_PERCENTAGE;
                    account.addFunds(interest);
                    Utils.addTransactionForAddingInterest(objectMapper, user, command, interest);
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
            user.removeDuplicateTransactions(user.getTransactions());
            for (AccountType account : user.getAccounts()) {
                if (account.getIban().equals(command.getAccount())) {
                    user.removeTransactionsByIBAN(user.getTransactions(), account.getIban());
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
                    Utils.addOutput(result, output, command, null, outputNode);
                    return;
                }
            }
        }
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("description", "Account not found");
        outputNode.put("timestamp", command.getTimestamp());
        Utils.addOutput(result, output, command, null, outputNode);
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
            user.removeDuplicateTransactions(user.getTransactions());
            for (int a = 0; a < user.getAccounts().size(); a++) {
                AccountType account = user.getAccounts().get(a);
                if (account.getIban().equals(command.getAccount())) {
                    if (Objects.equals(account.getType(), "savings")) {
                        ObjectNode outputNode = objectMapper.createObjectNode();
                        outputNode.put("error",
                                "This kind of report is not supported for a saving account");
                        Utils.addOutput(result, output, command, null, outputNode);
                        return;
                    }
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
                            if (commerciants[j].compareTo(commerciants[j + 1]) > 0) {
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
                    Utils.addOutput(result, output, command, null, outputNode);
                    return;
                }
            }
        }
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("description", "Account not found");
        outputNode.put("timestamp", command.getTimestamp());
        Utils.addOutput(result, output, command, null, outputNode);
    }
}
