package org.poo.solution;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.poo.fileio.UserInput;
import java.util.ArrayList;
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

    public User(UserInput userInput) {
        this.firstName = userInput.getFirstName();
        this.lastName = userInput.getLastName();
        this.email = userInput.getEmail();
        this.accounts = new ArrayList<>();
    }
}
