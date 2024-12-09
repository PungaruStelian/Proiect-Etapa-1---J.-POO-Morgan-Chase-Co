package org.poo.solution;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.utils.Utils;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OneTimeCard extends Card {
    @JsonIgnore
    private boolean isUsed;

    /**
     * Method to mark the card as used
     * It sets the isUsed attribute to true and generates a new card number
     */
    public void markAsUsed() {
        this.isUsed = true;
        this.setCardNumber(Utils.generateCardNumber());
    }

    /**
     * Builder class for OneTimeCards
     * Extends the CardBuilder class
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class OneTimeCardBuilder extends Card.CardBuilder {
        private boolean isUsed;

        /**
         * Method to set the isUsed attribute
         * @param otherUse The isUsed attribute to set
         * @return The OneTimeCardBuilder object
         */
        public OneTimeCardBuilder setUsed(final boolean otherUse) {
            this.isUsed = otherUse;
            return this;
        }

        /**
         * Method to set the card number
         * @param cardNumber The card number to set
         * @return The OneTimeCardBuilder object
         */
        @Override
        public OneTimeCardBuilder setCardNumber(final String cardNumber) {
            super.setCardNumber(cardNumber);
            return this;
        }

        /**
         * Method to set the status
         * @param status The status to set
         * @return The OneTimeCardBuilder object
         */
        @Override
        public OneTimeCardBuilder setStatus(final String status) {
            super.setStatus(status);
            return this;
        }

        /**
         * Method to build the OneTimeCard object
         * @return The OneTimeCard object
         */
        @Override
        public OneTimeCard build() {
            OneTimeCard oneTimeCard = new OneTimeCard();
            oneTimeCard.setCardNumber(this.getCardNumber());
            oneTimeCard.setStatus(this.getStatus());
            oneTimeCard.setUsed(this.isUsed);
            return oneTimeCard;
        }
    }
}
