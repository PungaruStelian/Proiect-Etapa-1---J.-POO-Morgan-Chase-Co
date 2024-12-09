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

    @Override
    public void makeFrozen() {
        this.setStatus("frozen");
    }

    @Override
    public void makeWarning() {
        this.setStatus("warning");
    }

    @Override
    public boolean ruFrozen() {
        return this.getStatus().equals("frozen");
    }

    /**
     * Builder class for OneTimeCards
     * Extends the CardBuilder class
     */

    @Data
    public static class OneTimeCardBuilder {
        private PermanentCard.PermanentCardBuilder cardBuilder = new PermanentCard.PermanentCardBuilder();
        private boolean isUsed;

        public OneTimeCardBuilder setUsed(final boolean otherUse) {
            this.isUsed = otherUse;
            return this;
        }

        public OneTimeCardBuilder setCardNumber(final String cardNumber) {
            cardBuilder.setCardNumber(cardNumber);
            return this;
        }

        public OneTimeCardBuilder setStatus(final String status) {
            cardBuilder.setStatus(status);
            return this;
        }

        public OneTimeCard build() {
            OneTimeCard oneTimeCard = new OneTimeCard();
            oneTimeCard.setCardNumber(cardBuilder.getCardNumber());
            oneTimeCard.setStatus(cardBuilder.getStatus());
            oneTimeCard.setUsed(this.isUsed);
            return oneTimeCard;
        }
    }
}
