package org.poo.solution;

import org.poo.utils.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

// Singleton
public class Handle {
    private static Handle instance = null;
    private final ObjectMapper objectMapper;

    private Handle() {
        objectMapper = new ObjectMapper();
    }

    public static Handle getInstance() {
        if (instance == null) {
            return new Handle();
        }
        return instance;
    }

    public void createTransaction(Object object){
        for (User user : object.getUsers()) {
            user.setTransactions(objectMapper.createArrayNode());

        }
    }

    public void printUsers(Object object, Command command, ObjectNode result, ArrayNode output) {
        ArrayNode usersArray = objectMapper.createArrayNode();
        for (User user : object.getUsers()) {
            ObjectNode userNode = objectMapper.valueToTree(user);
            usersArray.add(userNode);
        }
        result.set("output", usersArray);
        result.put("timestamp", command.getTimestamp());
        output.add(result);
    }

    public void addAccount(Object object, Command command) {
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
                ObjectNode transaction = objectMapper.createObjectNode();
                transaction.put("timestamp", command.getTimestamp());
                transaction.put("description", "New account created");
                user.getTransactions().add(transaction);
            }
        }
    }

    public void deleteAccount(Object object, Command command, ObjectNode result, ArrayNode output) {
        for (User user : object.getUsers()) {
            List<Account> accounts = user.getAccounts();
            for (int i = 0; i < accounts.size(); i++) {
                Account account = accounts.get(i);
                if (account.getIBAN().equals(command.getAccount()) && account.getBalance() == 0) {
                    accounts.remove(i);

                    ObjectNode outputNode = objectMapper.createObjectNode();
                    outputNode.put("success", "Account deleted");
                    outputNode.put("timestamp", command.getTimestamp());
                    result.set("output", outputNode);
                    result.put("timestamp", command.getTimestamp());
                    output.add(result);

                    return;
                }
            }
        }
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("error", "Account couldn't be deleted - see org.poo.transactions for details");
        outputNode.put("timestamp", command.getTimestamp());
        result.set("output", outputNode);
        result.put("timestamp", command.getTimestamp());
        output.add(result);
    }

    public void createCard(Object object, Command command) {
        for (User user : object.getUsers()) {
            if (user.getEmail().equals(command.getEmail())) {
                for (Account account : user.getAccounts()) {
                    if (account.getIBAN().equals(command.getAccount())) {
                        Card newCard = new Card.CardBuilder()
                                .setCardNumber(Utils.generateCardNumber())
                                .setCardHolder(user.getFirstName() + " " + user.getLastName())
                                .setStatus("active")
                                .build();
                        account.getCards().add(newCard);
                        ObjectNode transaction = objectMapper.createObjectNode();
                        transaction.put("timestamp", command.getTimestamp());
                        transaction.put("description", "New card created");
                        transaction.put("account", account.getIBAN());
                        transaction.put("cardHolder", user.getEmail());
                        transaction.put("card", newCard.getCardNumber());
                        user.getTransactions().add(transaction);
                    }
                }
            }
        }
    }

    public void addFunds(Object object, Command command) {
        for (User user : object.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().equals(command.getAccount())) {
                    account.addFunds(command.getAmount());
                }
            }
        }
    }

    public void deleteCard(Object object, Command command) {
        for (User user : object.getUsers()) {
            for (Account account : user.getAccounts()) {
                List<Card> cards = account.getCards();
                for (int i = 0; i < cards.size(); i++) {
                    Card currentCard = cards.get(i);
                    if (currentCard.getCardNumber().equals(command.getCardNumber())) {
                        cards.remove(i);
                        ObjectNode transaction = objectMapper.createObjectNode();
                        transaction.put("timestamp", command.getTimestamp());
                        transaction.put("description", "The card has been destroyed");
                        transaction.put("account", account.getIBAN());
                        transaction.put("cardHolder", user.getEmail());
                        transaction.put("card", currentCard.getCardNumber());
                        user.getTransactions().add(transaction);
                        break;
                    }
                }
            }
        }
    }

    public void createOneTimeCard(Object object, Command command) {
        for (User user : object.getUsers()) {
            if (user.getEmail().equals(command.getEmail())) {
                for (Account account : user.getAccounts()) {
                    if (account.getIBAN().equals(command.getAccount())) {
                        OneTimeCard newCard = new OneTimeCard.OneTimeCardBuilder()
                                .setUsed(false)
                                .setCardNumber(Utils.generateCardNumber())
                                .setCardHolder(user.getFirstName() + " " + user.getLastName())
                                .setStatus("active")
                                .build();
                        account.getCards().add(newCard);
                        ObjectNode transaction = objectMapper.createObjectNode();
                        transaction.put("timestamp", command.getTimestamp());
                        transaction.put("description", "New card created");
                        transaction.put("account", account.getIBAN());
                        transaction.put("cardHolder", user.getEmail());
                        transaction.put("card", newCard.getCardNumber());
                        user.getTransactions().add(transaction);
                    }
                }
            }
        }
    }

    public void setMinimumBalance(Object object, Command command) {
        for (User user : object.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().equals(command.getAccount())) {
                    account.setMinBalance(command.getMinBalance());
                    break;
                }
            }
        }
    }

    public void payOnline(Object object, Command command, ObjectNode result, ArrayNode output) {
        for (User user : object.getUsers()) {
            for (Account account : user.getAccounts()) {
                for(Card card : account.getCards()) {
                    if(Objects.equals(card.getCardNumber(), command.getCardNumber())) {
                        if (!Objects.equals(command.getEmail(), user.getEmail())) {
                            ObjectNode outputNode = objectMapper.createObjectNode();
                            outputNode.put("description", "The card does not belong to the user");
                            outputNode.put("timestamp", command.getTimestamp());
                            result.set("output", outputNode);
                            result.put("timestamp", command.getTimestamp());
                            output.add(result);
                            return;
                        }
                        if (card instanceof OneTimeCard && ((OneTimeCard) card).isUsed()) {
                            ObjectNode outputNode = objectMapper.createObjectNode();
                            outputNode.put("description", "Card already used");
                            outputNode.put("timestamp", command.getTimestamp());
                            result.set("output", outputNode);
                            result.put("timestamp", command.getTimestamp());
                            output.add(result);
                            return;
                        }
                        if(card.ruFrozen()) {
                            ObjectNode transaction = objectMapper.createObjectNode();
                            transaction.put("timestamp", command.getTimestamp());
                            transaction.put("description", "The card is frozen");
                            user.getTransactions().add(transaction);
                            return;
                        }
                        double exhg;
                        if(!Objects.equals(command.getCurrency(), account.getCurrency()))
                        {
                            exhg = account.getExchange(object, command.getCurrency(), account.getCurrency(), command.getAmount());
                        } else {
                            exhg = command.getAmount();
                        }
                        if (!account.withdrawFunds(exhg)) {
                            ObjectNode transaction = objectMapper.createObjectNode();
                            transaction.put("timestamp", command.getTimestamp());
                            transaction.put("description", "Insufficient funds");
                            user.getTransactions().add(transaction);
                            return;
                        }
                        if(card instanceof OneTimeCard) {
                            ((OneTimeCard) card).setUsed(true);
                        }
                        if(account.getBalance() <= account.getMinBalance()) {
                            card.makeFrozen();
                            ObjectNode transaction = objectMapper.createObjectNode();
                            transaction.put("timestamp", command.getTimestamp());
                            transaction.put("description", "You have reached the minimum amount of funds, the card will be frozen");
                            user.getTransactions().add(transaction);
                            return;
                        }
                        if(account.getBalance() <= account.getMinBalance() + 30) {
                            card.makeWarning();
                            ObjectNode transaction = objectMapper.createObjectNode();
                            transaction.put("timestamp", command.getTimestamp());
                            transaction.put("description", "You have reached the minimum amount of funds + 30, you will receive a warning");
                            user.getTransactions().add(transaction);
                            return;
                        }
                        ObjectNode transaction = objectMapper.createObjectNode();
                        transaction.put("timestamp", command.getTimestamp());
                        transaction.put("description", "Card payment");
                        transaction.put("amount", exhg);
                        transaction.put("commerciant", command.getCommerciant());
                        user.getTransactions().add(transaction);
                        return;
                    }
                }
            }
        }
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("description", "Card not found");
        outputNode.put("timestamp", command.getTimestamp());
        result.set("output", outputNode);
        result.put("timestamp", command.getTimestamp());
        output.add(result);
    }

    public void sendMoney(Object object, Command command) {
        for (User user : object.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().equals(command.getAccount())) {
                    for (User receiver : object.getUsers()) {
                        for (Account receiverAccount : receiver.getAccounts()) {
                            if (receiverAccount.getIBAN().equals(command.getReceiver()) || (receiverAccount.getAlias() != null && receiverAccount.getAlias().equals(command.getAccount()))) {
                                double exhg;
                                if (!Objects.equals(receiverAccount.getCurrency(), account.getCurrency())) {
                                    exhg = account.getExchange(object, account.getCurrency(), receiverAccount.getCurrency(), command.getAmount());
                                } else {
                                    exhg = command.getAmount();
                                }
                                if(!account.transferFunds(receiverAccount, command.getAmount(), exhg))
                                {
                                    ObjectNode transaction = objectMapper.createObjectNode();
                                    transaction.put("timestamp", command.getTimestamp());
                                    transaction.put("description", "Insufficient funds");
                                    user.getTransactions().add(transaction);
                                    return;
                                }

                                // Add the specified JSON object to the transactions
                                ObjectNode transaction = objectMapper.createObjectNode();
                                transaction.put("timestamp", command.getTimestamp());
                                transaction.put("description", command.getDescription());
                                transaction.put("senderIBAN", account.getIBAN());
                                transaction.put("receiverIBAN", receiverAccount.getIBAN());
                                transaction.put("amount", command.getAmount() + " " + account.getCurrency());
                                transaction.put("transferType", "sent");
                                user.getTransactions().add(transaction);
                                // Add the specified JSON object to the transactions
                                ObjectNode receiverTransaction = objectMapper.createObjectNode();
                                receiverTransaction.put("timestamp", command.getTimestamp());
                                receiverTransaction.put("description", command.getDescription());
                                receiverTransaction.put("senderIBAN", account.getIBAN());
                                receiverTransaction.put("receiverIBAN", receiverAccount.getIBAN());
                                receiverTransaction.put("amount", command.getAmount() + " " + account.getCurrency());
                                receiverTransaction.put("transferType", "received");
                                receiver.getTransactions().add(receiverTransaction);
                                return;
                            }
                        }
                    }
                    return;
                }
            }
        }
    }

    public void setAlias(Object object, Command command) {
        for (User user : object.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().equals(command.getAccount())) {
                    account.setAlias(command.getAlias());
                    return;
                }
            }
        }
    }

    public void printTransactions(Object object, Command command, ObjectNode result, ArrayNode output) {
        for (User user : object.getUsers()) {
            if (user.getEmail().equals(command.getEmail())) {
                ArrayNode filteredTransactions = objectMapper.createArrayNode();
                for (int i = 0; i < user.getTransactions().size(); i++) {
                    if (user.getTransactions().get(i).get("timestamp").asInt() < command.getTimestamp()) {
                        filteredTransactions.add(user.getTransactions().get(i));
                    }
                }
                result.put("command", "printTransactions");
                result.set("output", filteredTransactions);
                result.put("timestamp", command.getTimestamp());
                output.add(result);
                return;
            }
        }
    }

    public void checkCardStatus(Object object, Command command, ObjectNode result, ArrayNode output) {
        for (User user : object.getUsers()) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(command.getCardNumber())) {
                        if(account.getBalance() <= account.getMinBalance()) {
                            card.makeFrozen();
                            ObjectNode transaction = objectMapper.createObjectNode();
                            transaction.put("timestamp", command.getTimestamp());
                            transaction.put("description", "You have reached the minimum amount of funds, the card will be frozen");
                            user.getTransactions().add(transaction);
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
        result.set("output", outputNode);
        result.put("timestamp", command.getTimestamp());
        output.add(result);
    }

    public void splitPayment(Object object, Command command, ObjectNode result, ArrayNode output) {
        for (User user : object.getUsers()) {
            for (Account account : user.getAccounts()) {
                for(String iban : command.getAccounts()) {
                    if(account.getIBAN().equals(iban)) {
                        double exhg;
                        if (!Objects.equals(account.getCurrency(), command.getCurrency())) {
                            exhg = account.getExchange(object, command.getCurrency(), account.getCurrency(), command.getAmount() / command.getAccounts().size());
                        } else {
                            exhg = command.getAmount() / command.getAccounts().size();
                        }
                        if(!account.withdrawFunds(exhg))
                        {
                            ObjectNode transaction = objectMapper.createObjectNode();
                            transaction.put("timestamp", command.getTimestamp());
                            transaction.put("description", "Insufficient funds");
                            user.getTransactions().add(transaction);
                            return;
                        }
                        ObjectNode transaction = objectMapper.createObjectNode();
                        transaction.put("timestamp", command.getTimestamp());
                        transaction.put("description", "Split payment of " + String.format(Locale.US, "%.2f", command.getAmount()) + " " + command.getCurrency());
                        transaction.put("amount", command.getAmount() / command.getAccounts().size());
                        transaction.put("currency", command.getCurrency());
                        ArrayNode involvedAccounts = objectMapper.createArrayNode();
                        for (String involvedIban : command.getAccounts()) {
                            involvedAccounts.add(involvedIban);
                        }
                        transaction.set("involvedAccounts", involvedAccounts);
                        user.getTransactions().add(transaction);
                    }
                }
            }
        }
    }

    public void changeInterestRate(Object object, Command command, ObjectNode result, ArrayNode output) {
        for (User user : object.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().equals(command.getAccount())) {
                    if(!Objects.equals(account.getType(), "savings"))
                    {
                        result.put("description", "This is not a savings account");
                        result.put("timestamp", command.getTimestamp());
                        output.add(result);
                        return;
                    }
                    account.setInterestRate(command.getInterestRate());
                    return;
                }
            }
        }
    }

    public void addInterest(Object object, Command command, ObjectNode result, ArrayNode output) {
        for (User user : object.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().equals(command.getAccount())) {
                    if (!Objects.equals(account.getType(), "savings")) {
                        result.put("description", "This is not a savings account");
                        result.put("timestamp", command.getTimestamp());
                        output.add(result);
                        return;
                    }
                    double interest = account.getBalance() * account.getInterestRate() / 100;
                    account.addFunds(interest);
                    ObjectNode transaction = objectMapper.createObjectNode();
                    transaction.put("timestamp", command.getTimestamp());
                    transaction.put("description", "Interest added");
                    transaction.put("amount", interest);
                    user.getTransactions().add(transaction);
                    return;
                }
            }
        }
    }

    public void report(Object object, Command command, ObjectNode result, ArrayNode output) {
        for (User user : object.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().equals(command.getAccount())) {
                    ArrayNode filteredTransactions = objectMapper.createArrayNode();
                    for (int i = 0; i < user.getTransactions().size(); i++) {
                        ObjectNode transaction = (ObjectNode) user.getTransactions().get(i);
                        int timestamp = transaction.get("timestamp").asInt();
                        if (timestamp >= command.getStartTimestamp() && timestamp <= command.getEndTimestamp()) {
                            filteredTransactions.add(transaction);
                        }
                    }
                    ObjectNode outputNode = objectMapper.createObjectNode();
                    outputNode.put("IBAN", account.getIBAN());
                    outputNode.put("balance", account.getBalance());
                    outputNode.put("currency", account.getCurrency());
                    outputNode.set("transactions", filteredTransactions);
                    result.set("output", outputNode);
                    result.put("timestamp", command.getTimestamp());
                    output.add(result);
                    return;
                }
            }
        }
    }

    public void spendingsReport(Object object, Command command, ObjectNode result, ArrayNode output) {
        for (User user : object.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().equals(command.getAccount())) {
                    ArrayNode filteredTransactions = objectMapper.createArrayNode();
                    Map<String, Double> commerciantSpendings = new HashMap<>();

                    for (int i = 0; i < user.getTransactions().size(); i++) {
                        ObjectNode transaction = (ObjectNode) user.getTransactions().get(i);
                        int timestamp = transaction.get("timestamp").asInt();
                        if (timestamp >= command.getStartTimestamp() && timestamp <= command.getEndTimestamp() &&
                                transaction.has("commerciant")) {
                            filteredTransactions.add(transaction);
                            String commerciant = transaction.get("commerciant").asText();
                            double amount = transaction.get("amount").asDouble();
                            commerciantSpendings.put(commerciant, commerciantSpendings.getOrDefault(commerciant, 0.0) + amount);
                        }
                    }

                    List<Map.Entry<String, Double>> sortedCommerciants = new ArrayList<>(commerciantSpendings.entrySet());
                    sortedCommerciants.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));

                    ArrayNode commerciantsArray = objectMapper.createArrayNode();
                    for (Map.Entry<String, Double> entry : sortedCommerciants) {
                        ObjectNode commerciantNode = objectMapper.createObjectNode();
                        commerciantNode.put("commerciant", entry.getKey());
                        commerciantNode.put("total", entry.getValue());
                        commerciantsArray.add(commerciantNode);
                    }

                    ObjectNode outputNode = objectMapper.createObjectNode();
                    outputNode.put("IBAN", account.getIBAN());
                    outputNode.put("balance", account.getBalance());
                    outputNode.put("currency", account.getCurrency());
                    outputNode.set("transactions", filteredTransactions);
                    outputNode.set("commerciants", commerciantsArray);
                    result.set("output", outputNode);
                    result.put("timestamp", command.getTimestamp());
                    output.add(result);
                    return;
                }
            }
        }
    }
}
