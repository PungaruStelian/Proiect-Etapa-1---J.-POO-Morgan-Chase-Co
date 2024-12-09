package org.poo.solution;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class AbstractCard {
    private String cardNumber;
    private String status;

    public abstract void makeFrozen();
    public abstract void makeWarning();
    public abstract boolean ruFrozen();
}