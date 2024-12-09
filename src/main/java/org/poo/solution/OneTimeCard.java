package org.poo.solution;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.utils.Utils;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OneTimeCard extends AbstractCard {
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

    @Data
    public static class OneTimeCardBuilder {
        private PermanentCard.PermanentCardBuilder cardBuilder
                = new PermanentCard.PermanentCardBuilder();
        private boolean isUsed;

        /**
         * Method to set the used attribute
         * @param otherUse The value to set
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
        public OneTimeCardBuilder setCardNumber(final String cardNumber) {
            cardBuilder.setCardNumber(cardNumber);
            return this;
        }

        /**
         * Method to set the status
         * @param status The status to set
         * @return The OneTimeCardBuilder object
         */
        public OneTimeCardBuilder setStatus(final String status) {
            cardBuilder.setStatus(status);
            return this;
        }

        /**
         * Method to build the OneTimeCard object
         * @return The OneTimeCard object
         */
        public OneTimeCard build() {
            OneTimeCard oneTimeCard = new OneTimeCard();
            oneTimeCard.setCardNumber(cardBuilder.getCardNumber());
            oneTimeCard.setStatus(cardBuilder.getStatus());
            oneTimeCard.setUsed(this.isUsed);
            return oneTimeCard;
        }
    }
}
