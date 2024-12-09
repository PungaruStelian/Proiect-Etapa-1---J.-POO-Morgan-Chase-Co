package org.poo.solution;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PermanentCard extends AbstractCard implements CardStatus {

    /**
     * Builder class for Cards
     */
    @Data
    public static class PermanentCardBuilder {
        private String cardNumber;
        private String expirationDate;
        private String status;
        private int cvv;

        /**
         * Method to set the card number
         * @param otherCardNumber The card number to set
         * @return The CardBuilder object
         */
        public PermanentCardBuilder setCardNumber(final String otherCardNumber) {
            this.cardNumber = otherCardNumber;
            return this;
        }

        /**
         * Method to set status
         * @param otherStatus The status to set
         * @return The CardBuilder object
         */
        public PermanentCardBuilder setStatus(final String otherStatus) {
            this.status = otherStatus;
            return this;
        }

        /**
         * Method to build the Card object
         * @return The Card object
         */
        public PermanentCard build() {
            PermanentCard card = new PermanentCard();
            card.setCardNumber(this.cardNumber);
            card.setStatus(this.status);
            return card;
        }
    }

    /**
     * Method to check if the card is frozen
     * @return true if the card is frozen, false otherwise
     */
    @Override
    public boolean ruFrozen() {
        return this.getStatus().equals("frozen");
    }

    /**
     * Method to set the card as frozen
     */
    @Override
    public void makeFrozen() {
        this.setStatus("frozen");
    }

    /**
     * Method to set the card as warning
     */
    @Override
    public void makeWarning() {
        this.setStatus("warning");
    }
}
