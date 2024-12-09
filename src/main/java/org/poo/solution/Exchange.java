package org.poo.solution;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.poo.fileio.ExchangeInput;

@Data
@NoArgsConstructor
public class Exchange {
    private String from;
    private String to;
    private double rate;
    private int timestamp;

    /**
     * Constructor for Exchange
     * @param exchangeInput The ExchangeInput object
     */
    public Exchange(final ExchangeInput exchangeInput) {
        this.from = exchangeInput.getFrom();
        this.to = exchangeInput.getTo();
        this.rate = exchangeInput.getRate();
        this.timestamp = exchangeInput.getTimestamp();
    }
}
