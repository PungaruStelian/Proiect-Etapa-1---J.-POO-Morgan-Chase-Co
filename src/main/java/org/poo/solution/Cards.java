package org.poo.solution;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

// Builder
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
    @JsonIgnore
    private boolean isUsed;

    // Builder class for Cards
    public static class CardsBuilder {
        private String cardNumber;
        private String cardHolder;
        private String expirationDate;
        private int cvv;
        private double balance;
        private String status;
        private boolean isPermanent;
        private boolean isUsed;

        public CardsBuilder setUsed(boolean isUsed) {
            this.isUsed = isUsed;
            return this;
        }

        public CardsBuilder setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
            return this;
        }

        public CardsBuilder setCardHolder(String cardHolder) {
            this.cardHolder = cardHolder;
            return this;
        }

        public CardsBuilder setExpirationDate(String expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

        public CardsBuilder setCvv(int cvv) {
            this.cvv = cvv;
            return this;
        }

        public CardsBuilder setBalance(double balance) {
            this.balance = balance;
            return this;
        }

        public CardsBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public CardsBuilder setPermanent(boolean isPermanent) {
            this.isPermanent = isPermanent;
            return this;
        }

        public Cards build() {
            Cards card = new Cards();
            card.cardNumber = this.cardNumber;
            card.cardHolder = this.cardHolder;
            card.expirationDate = this.expirationDate;
            card.cvv = this.cvv;
            card.balance = this.balance;
            card.status = this.status;
            card.isPermanent = this.isPermanent;
            card.isUsed = this.isUsed;
            return card;
        }
    }

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