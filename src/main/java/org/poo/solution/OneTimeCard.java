package org.poo.solution;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

// Builder
@Data
@NoArgsConstructor
public class OneTimeCard extends Cards {
    @JsonIgnore
    private boolean isPermanent;
    @JsonIgnore
    private boolean isUsed;

    @Data
    public static class OneTimeCardBuilder extends Cards.CardsBuilder {
        private boolean isPermanent;
        private boolean isUsed;

        public OneTimeCardBuilder setPermanent(boolean isPermanent) {
            this.isPermanent = isPermanent;
            return this;
        }

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
            oneTimeCard.setBalance(this.getBalance());
            oneTimeCard.setStatus(this.getStatus());
            oneTimeCard.setPermanent(this.isPermanent);
            oneTimeCard.setUsed(this.isUsed);
            return oneTimeCard;
        }
    }
}