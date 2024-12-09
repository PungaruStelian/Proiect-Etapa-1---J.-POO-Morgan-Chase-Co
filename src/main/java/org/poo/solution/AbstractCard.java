package org.poo.solution;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class AbstractCard {
    private String cardNumber;
    private String status;
}
