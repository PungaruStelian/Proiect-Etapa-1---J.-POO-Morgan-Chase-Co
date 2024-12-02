package org.poo.solution;

import org.poo.utils.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

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
        boolean ok = false;
        for (User user : object.getUsers()) {
            List<Account> accounts = user.getAccounts();
            // Iterăm prin indici pentru a permite eliminarea elementelor din listă
            for (int i = 0; i < accounts.size(); i++) {
                Account account = accounts.get(i);
                if (account.getIBAN().equals(command.getAccount()) && account.getBalance() == 0) {
                    accounts.remove(i);
                    ok = true;

                    ObjectNode outputNode = objectMapper.createObjectNode();
                    outputNode.put("success", "Account deleted");
                    outputNode.put("timestamp", command.getTimestamp());
                    result.set("output", outputNode);
                    result.put("timestamp", command.getTimestamp());
                    output.add(result);

                    break; // Ieșim din bucla interioară după ștergere
                }
            }
        }
        if (!ok) {
            result.put("description", "User not found");
            result.put("timestamp", command.getTimestamp());
            output.add(result);
        }
    }

    public void createCard(Object object, Command command) {
        for (User user : object.getUsers()) {
            if (user.getEmail().equals(command.getEmail())) {
                for (Account account : user.getAccounts()) {
                    if (account.getIBAN().equals(command.getAccount())) {
                        Cards newCard = new Cards.CardsBuilder()
                                .setCardNumber(Utils.generateCardNumber())
                                .setStatus("active")
                                .setPermanent(true)
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
                                .setStatus("active")
                                .setPermanent(false)
                                .build();
                        account.getCards().add(newCard);
                    }
                }
            }
        }
    }

    public void setMinimumBalance(Object object, Command command, ObjectNode result, ArrayNode output) {
        boolean found = false;
        for (User user : object.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().equals(command.getAccount())) {
                    account.setMinBalance(command.getMinBalance());
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            result.put("error", "Account not found");
            output.add(result);
        }
    }
}
