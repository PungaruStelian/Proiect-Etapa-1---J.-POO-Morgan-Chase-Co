package org.poo.solution;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.utils.Utils;

import java.util.Iterator;
import java.util.List;

public class Handle {
    private final ObjectMapper objectMapper = new ObjectMapper();

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
                Account newAccount = new Account();
                newAccount.setIBAN(Utils.generateIBAN());
                newAccount.setBalance(0.0);
                newAccount.setCurrency(command.getCurrency());
                newAccount.setType(command.getAccountType());
                newAccount.activate();
                user.getAccounts().add(newAccount);
            }
        }
    }

    public void deleteAccount(Object object, Command command, ObjectNode result, ArrayNode output) {
        boolean ok = false;
        for (User user : object.getUsers()) {
            List<Account> accounts = user.getAccounts();
            for (Iterator<Account> iterator = accounts.iterator(); iterator.hasNext(); ) {
                Account account = iterator.next();
                if (account.getIBAN().equals(command.getAccount()) && account.getBalance() == 0) {
                    iterator.remove();
                    ok = true;

                    ObjectNode outputNode = objectMapper.createObjectNode();
                    outputNode.put("success", "Account deleted");
                    outputNode.put("timestamp", command.getTimestamp());
                    result.set("output", outputNode);
                    result.put("timestamp", command.getTimestamp());
                    output.add(result);

                    break;
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
                        Cards newCard = new Cards();
                        newCard.setCardNumber(Utils.generateCardNumber());
                        newCard.activate();
                        newCard.setPermanent(true);
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
                for (Iterator<Cards> iterator = cards.iterator(); iterator.hasNext(); ) {
                    Cards card = iterator.next();
                    if (card.getCardNumber().equals(command.getCardNumber())) {
                        iterator.remove();
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
                        Cards newCard = new Cards();
                        newCard.setCardNumber(Utils.generateCardNumber());
                        newCard.activate();
                        newCard.setPermanent(false);
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
