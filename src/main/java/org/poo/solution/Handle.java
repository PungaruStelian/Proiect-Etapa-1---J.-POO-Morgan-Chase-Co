package org.poo.solution;

import org.poo.utils.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.Objects;

// Singleton
public class Handle {
    private static final Handle instance = null;
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

    public void printUsers(Object object, ObjectNode result) {
        ArrayNode usersArray = objectMapper.createArrayNode();
        for (User user : object.getUsers()) {
            ObjectNode userNode = objectMapper.valueToTree(user);
            usersArray.add(userNode);
        }
        result.set("output", usersArray);
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
            }
        }
    }

    public void deleteAccount(Object object, Command command, ObjectNode result, ArrayNode output) {
        for (User user : object.getUsers()) {
            List<Account> accounts = user.getAccounts();
            // Iterăm prin indici pentru a permite eliminarea elementelor din listă
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
        result.put("description", "User not found");
        result.put("timestamp", command.getTimestamp());
        output.add(result);
    }

    public void createCard(Object object, Command command) {
        for (User user : object.getUsers()) {
            if (user.getEmail().equals(command.getEmail())) {
                for (Account account : user.getAccounts()) {
                    if (account.getIBAN().equals(command.getAccount())) {
                        Cards newCard = new Cards.CardsBuilder()
                                .setCardNumber(Utils.generateCardNumber())
                                .setCardHolder(user.getFirstName() + " " + user.getLastName())
                                .setStatus("active")
                                .setPermanent(true)
                                .setUsed(false)
                                .build();
                        account.getCards().add(newCard);
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
                List<Cards> cards = account.getCards();
                // Iterăm prin indici pentru a permite eliminarea elementelor din listă
                for (int i = 0; i < cards.size(); i++) {
                    Cards card = cards.get(i);
                    if (card.getCardNumber().equals(command.getCardNumber())) {
                        cards.remove(i);
                        break; // Ieșim din bucla interioară după ștergere
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
                        Cards newCard = new Cards.CardsBuilder()
                                .setCardNumber(Utils.generateCardNumber())
                                .setCardHolder(user.getFirstName() + " " + user.getLastName())
                                .setStatus("active")
                                .setPermanent(false)
                                .setUsed(false)
                                .build();
                        account.getCards().add(newCard);
                    }
                }
            }
        }
    }

    public void setMinimumBalance(Object object, Command command, ObjectNode result, ArrayNode output) {
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
                for(Cards card : account.getCards()) {
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
                        if (card.isUsed()) {
                            ObjectNode outputNode = objectMapper.createObjectNode();
                            outputNode.put("description", "Card already used");
                            outputNode.put("timestamp", command.getTimestamp());
                            result.set("output", outputNode);
                            result.put("timestamp", command.getTimestamp());
                            output.add(result);
                            return;
                        }
//                        if (command.getAmount() > account.getBalance()) {
//                            ObjectNode outputNode = objectMapper.createObjectNode();
//                            outputNode.put("description", "Insufficient funds");
//                            outputNode.put("timestamp", command.getTimestamp());
//                            result.set("output", outputNode);
//                            result.put("timestamp", command.getTimestamp());
//                            output.add(result);
//                            return;
//                        }

//                        if (account.getMinBalance() > account.getBalance() - command.getAmount()) {
//                            ObjectNode outputNode = objectMapper.createObjectNode();
//                            outputNode.put("description", "Minimum balance not met");
//                            outputNode.put("timestamp", command.getTimestamp());
//                            result.set("output", outputNode);
//                            result.put("timestamp", command.getTimestamp());
//                            output.add(result);
//                            return;
//                        }
                        account.withdrawFunds(account.getExchange(object, command.getCurrency(), account.getCurrency(), command.getAmount()));
                        if(!card.isPermanent()) {
                            card.setUsed(true);
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
}
