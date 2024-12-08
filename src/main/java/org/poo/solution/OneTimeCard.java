package org.poo.solution;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.utils.Utils;

// Builder
@Data
@NoArgsConstructor
public class OneTimeCard extends Card {
    @JsonIgnore
    private boolean isUsed;

    public void markAsUsed() {
        this.isUsed = true;
        this.setCardNumber(Utils.generateCardNumber());
    }

    @Data
    public static class OneTimeCardBuilder extends Card.CardBuilder {
        private boolean isUsed;

        public OneTimeCardBuilder setUsed(boolean isUsed) {
            this.isUsed = isUsed;
            return this;
        }

        @Override
        public OneTimeCardBuilder setCardNumber(String cardNumber) {
            super.setCardNumber(cardNumber);
            return this;
        }

        @Override
        public OneTimeCardBuilder setCardHolder(String cardHolder) {
            super.setCardHolder(cardHolder);
            return this;
        }

        @Override
        public OneTimeCardBuilder setStatus(String status) {
            super.setStatus(status);
            return this;
        }

        @Override
        public OneTimeCard build() {
            OneTimeCard oneTimeCard = new OneTimeCard();
            oneTimeCard.setCardNumber(this.getCardNumber());
            oneTimeCard.setCardHolder(this.getCardHolder());
            oneTimeCard.setExpirationDate(this.getExpirationDate());
            oneTimeCard.setCvv(this.getCvv());
            oneTimeCard.setStatus(this.getStatus());
            oneTimeCard.setUsed(this.isUsed);
            return oneTimeCard;
        }
    }
}