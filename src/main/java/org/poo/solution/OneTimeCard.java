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

    public static class OneTimeCardBuilder {
        private String cardNumber;
        private String cardHolder;
        private String expirationDate;
        private int cvv;
        private double balance;
        private String status;
        private boolean isPermanent;
        private boolean isUsed;

        public OneTimeCardBuilder setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
            return this;
        }

        public OneTimeCardBuilder setCardHolder(String cardHolder) {
            this.cardHolder = cardHolder;
            return this;
        }

        public OneTimeCardBuilder setExpirationDate(String expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

        public OneTimeCardBuilder setCvv(int cvv) {
            this.cvv = cvv;
            return this;
        }

        public OneTimeCardBuilder setBalance(double balance) {
            this.balance = balance;
            return this;
        }

        public OneTimeCardBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public OneTimeCardBuilder setPermanent(boolean isPermanent) {
            this.isPermanent = isPermanent;
            return this;
        }

        public OneTimeCardBuilder setUsed(boolean isUsed) {
            this.isUsed = isUsed;
            return this;
        }

        public OneTimeCard build() {
            OneTimeCard oneTimeCard = new OneTimeCard();
            oneTimeCard.setCardNumber(this.cardNumber);
            oneTimeCard.setCardHolder(this.cardHolder);
            oneTimeCard.setExpirationDate(this.expirationDate);
            oneTimeCard.setCvv(this.cvv);
            oneTimeCard.setBalance(this.balance);
            oneTimeCard.setStatus(this.status);
            oneTimeCard.setPermanent(this.isPermanent);
            oneTimeCard.setUsed(this.isUsed);
            return oneTimeCard;
        }
    }
}