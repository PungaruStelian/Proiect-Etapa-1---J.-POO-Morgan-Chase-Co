package org.poo.solution;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.poo.fileio.ObjectInput;

// Singleton
@Data
@NoArgsConstructor
public class Object {
    private static Object instance = null;
    private Commerciant[] commerciants;
    private Exchange[] exchangeRates;
    private Command[] commands;
    private User[] users;

    public static Object getInstance(ObjectInput objectInput) {
        if (instance == null) {
            return new Object(objectInput);
        }
        return instance;
    }

    private Object(ObjectInput objectInput) {
        if (objectInput.getUsers() != null) {
            this.users = new User[objectInput.getUsers().length];
            for (int i = 0; i < objectInput.getUsers().length; i++) {
                this.users[i] = new User(objectInput.getUsers()[i]);
            }
        }

        if (objectInput.getExchangeRates() != null) {
            this.exchangeRates = new Exchange[objectInput.getExchangeRates().length];
            for (int i = 0; i < objectInput.getExchangeRates().length; i++) {
                this.exchangeRates[i] = new Exchange(objectInput.getExchangeRates()[i]);
            }
        }

        if (objectInput.getCommands() != null) {
            this.commands = new Command[objectInput.getCommands().length];
            for (int i = 0; i < objectInput.getCommands().length; i++) {
                this.commands[i] = new Command(objectInput.getCommands()[i]);
            }
        }

        if (objectInput.getCommerciants() != null) {
            this.commerciants = new Commerciant[objectInput.getCommerciants().length];
            for (int i = 0; i < objectInput.getCommerciants().length; i++) {
                this.commerciants[i] = new Commerciant(objectInput.getCommerciants()[i]);
            }
        }
    }
    public double getExchangeRate(String from, String to) {
        for (Exchange exchange : exchangeRates) {
            if (exchange.getFrom().equals(from) && exchange.getTo().equals(to)) {
                return exchange.getRate();
            }
            if(exchange.getFrom().equals(to) && exchange.getTo().equals(from)) {
                return 1 / exchange.getRate();
            }
        }
        if(exchangeRates[0].getFrom().equals(from) && exchangeRates[1].getTo().equals(to)) {
            return exchangeRates[0].getRate() * exchangeRates[1].getRate();
        }
        if(exchangeRates[0].getFrom().equals(to) && exchangeRates[1].getTo().equals(from)) {
            return 1 / (exchangeRates[0].getRate() * exchangeRates[1].getRate());
        }
        return 0;
    }
}
