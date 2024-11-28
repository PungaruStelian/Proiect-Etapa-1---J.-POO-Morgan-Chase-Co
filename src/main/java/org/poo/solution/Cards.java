package org.poo.solution;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@NoArgsConstructor
public class Cards {
    private String cardNumber;
    @JsonIgnore
    private String cardHolder;
    @JsonIgnore
    private String expirationDate;
    @JsonIgnore
    private int cvv;
    @JsonIgnore
    private double balance;
    private String status;
    @JsonIgnore
    private boolean isPermanent;

    // Method to activate the card
    public void activate() {
        this.status = "active";
    }

    // Method to deactivate the card
    public void deactivate() {
        this.status = "inactive";
    }

    // Validation methods
    private boolean isValidCardNumber(String cardNumber) {
        return cardNumber != null && cardNumber.matches("\\d{16}");
    }

    private boolean isValidExpirationDate(String expirationDate) {
        return expirationDate != null && expirationDate.matches("(0[1-9]|1[0-2])/\\d{2}");
    }

    private boolean isValidCvv(int cvv) {
        return cvv >= 100 && cvv <= 999;
    }
}