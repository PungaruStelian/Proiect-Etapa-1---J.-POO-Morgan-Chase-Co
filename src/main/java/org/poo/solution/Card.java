package org.poo.solution;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Card {
    private String cardNumber;
    private String status;

    /**
     * Builder class for Cards
     */
    @Data
    public static class CardBuilder {
        private String cardNumber;
        private String expirationDate;
        private String status;
        private int cvv;

        /**
         * Method to set the card number
         * @param otherCardNumber The card number to set
         * @return The CardBuilder object
         */
        public CardBuilder setCardNumber(final String otherCardNumber) {
            this.cardNumber = otherCardNumber;
            return this;
        }

        /**
         * Method to set status
         * @param otherStatus The status to set
         * @return The CardBuilder object
         */
        public CardBuilder setStatus(final String otherStatus) {
            this.status = otherStatus;
            return this;
        }

        /**
         * Method to build the Card object
         * @return The Card object
         */
        public Card build() {
            Card card = new Card();
            card.cardNumber = this.cardNumber;
            card.status = this.status;
            return card;
        }
    }

    /**
     * Method to check if the card is frozen
     * @return true if the card is frozen, false otherwise
     */
    public boolean ruFrozen() {
        return this.status.equals("frozen");
    }

    /**
     * Method to set the card as frozen
     */
    public void makeFrozen() {
        this.status = "frozen";
    }

    /**
     * Method to set the card as warning
     */
    public void makeWarning() {
        this.status = "warning";
    }
}
