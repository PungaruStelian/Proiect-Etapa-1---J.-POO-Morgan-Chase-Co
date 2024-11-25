package org.poo.solution;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.poo.fileio.UserInput;

@Data
@NoArgsConstructor
public class User {
    private String firstName;
    private String lastName;
    private String email;

    public User(UserInput userInput) {
        this.firstName = userInput.getFirstName();
        this.lastName = userInput.getLastName();
        this.email = userInput.getEmail();
    }
}
