package org.poo.solution;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.poo.fileio.UserInput;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

@Data
@NoArgsConstructor
public class User {
    @JsonIgnore
    private ArrayNode transactions;
    private String firstName;
    private String lastName;
    private String email;
    private List<Account> accounts;

    /**
     * Constructor for User
     * @param userInput The UserInput object
     */
    public User(final UserInput userInput) {
        this.firstName = userInput.getFirstName();
        this.lastName = userInput.getLastName();
        this.email = userInput.getEmail();
        this.accounts = new ArrayList<>();
    }

    /**
     * Method to eliminate duplicate transactions
     * @param transactions The transactions to check
     */
    public void removeDuplicateTransactions(ArrayNode transactions) {
        HashSet<String> uniqueTransactions = new HashSet<>();
        Iterator<JsonNode> iterator = transactions.iterator();
        while (iterator.hasNext()) {
            JsonNode node = iterator.next();
            if (node instanceof ObjectNode transaction) {
                String transactionString = transaction.toString();

                if (!uniqueTransactions.add(transactionString)) {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * Method to remove transactions by IBAN
     * @param transactions The transactions to check
     * @param iban The IBAN to remove
     */
    public void removeTransactionsByIBAN(ArrayNode transactions, String iban) {
        Iterator<JsonNode> iterator = transactions.iterator();
        while (iterator.hasNext()) {
            JsonNode node = iterator.next();
            if (node instanceof ObjectNode transaction) {
                JsonNode ibanNode = transaction.get("account");
                if (ibanNode != null && !ibanNode.asText().equals(iban)) {
                    iterator.remove();
                }
            }
        }
    }
}
