package org.poo.solution;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    @Override
    public void markAsUsed() {
        this.isUsed = true;
        this.setCardNumber(Utils.generateCardNumber());
    }

    /**
     * Method to check if the card is frozen
     * @return false
     */
    @Override
    public boolean ruFrozen() {
        return false;
    }

    /**
     * Method to set the card as frozen
     */
    @Override
    public void makeFrozen() {
        // Do nothing
    }

    /**
     * Method to set the card as warning
     */
    @Override
    public void makeWarning() {
        // Do nothing
    }

    /**
     * Method to check if the card is used
     * @return The value of the isUsed attribute
     */
    @Override
    public boolean ruUsed() {
        return this.isUsed;
    }

    /**
     * Method to handle the destruction of the card
     * @param objectMapper The ObjectMapper object
     * @param user The user that owns the card
     * @param command The command that triggered the destruction
     * @param account The account that owns the card
     * @param handle The handle object
     */
    @Override
    public void handleCardDestruction(final ObjectMapper objectMapper, final User user,
                                      final Command command, final AccountType account,
                                      final Handle handle) {
        Utils.addTransaction(objectMapper, user, command, "The card has been destroyed",
                account.getIban(), user.getEmail(), this.getCardNumber(),
                null, 0, null, null, null,
                null, null, null);
        this.markAsUsed();
        Utils.addTransaction(objectMapper, user, command, "New card created", account.getIban(),
                user.getEmail(), this.getCardNumber(), null, 0, null,
                null, null, null, null, null);
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
