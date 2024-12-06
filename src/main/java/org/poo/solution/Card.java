package org.poo.solution;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

// Builder
@Data
@NoArgsConstructor
public class Card {
    private String cardNumber;
    @JsonIgnore
    private String cardHolder;
    @JsonIgnore
    private String expirationDate;
    private String status;
    @JsonIgnore
    private double balance;
    @JsonIgnore
    private int cvv;

    // Builder class for Cards
    @Data
    public static class CardBuilder {
        private String cardNumber;
        private String cardHolder;
        private String expirationDate;
        private String status;
        private double balance;
        private int cvv;

        public CardBuilder setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
            return this;
        }

        public CardBuilder setCardHolder(String cardHolder) {
            this.cardHolder = cardHolder;
            return this;
        }

        public CardBuilder setExpirationDate(String expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

        public CardBuilder setCvv(int cvv) {
            this.cvv = cvv;
            return this;
        }

        public CardBuilder setBalance(double balance) {
            this.balance = balance;
            return this;
        }

        public CardBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Card build() {
            Card card = new Card();
            card.cardNumber = this.cardNumber;
            card.cardHolder = this.cardHolder;
            card.expirationDate = this.expirationDate;
            card.cvv = this.cvv;
            card.balance = this.balance;
            card.status = this.status;
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