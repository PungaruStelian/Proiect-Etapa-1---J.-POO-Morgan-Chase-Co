package org.poo.solution;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BankCard {
    private String cardNumber;
    private String cardHolder;
    private String expirationDate;
    private int cvv;
    private double balance;
    private boolean isActive;

    // Method to activate the card
    public void activate() {
        this.isActive = true;
    }

    // Method to deactivate the card
    public void deactivate() {
        this.isActive = false;
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