package org.poo.solution;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.poo.fileio.ObjectInput;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// Singleton
@Data
@NoArgsConstructor
public final class Object {
    private static Object instance = null;
    private Commerciant[] commerciants;
    private Exchange[] exchangeRates;
    private Command[] commands;
    private User[] users;

    /**
     * Get the instance of Object
     * @param objectInput The ObjectInput object
     * @return The instance of Object
     */
    public static Object getInstance(final ObjectInput objectInput) {
        if (instance == null) {
            return new Object(objectInput);
        }
        return instance;
    }

    /**
     * Constructor for Object
     * @param objectInput The ObjectInput object
     */
    private Object(final ObjectInput objectInput) {
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

    /**
     * Get the exchange rate between two currencies
     * @param from The currency to convert from
     * @param to The currency to convert to
     * @return The exchange rate
     */
    public double getExchangeRate(final String from, final String to) {
        // Use lists instead of arrays for dynamic resizing
        List<String> visitedCurrencies = new ArrayList<>();
        List<String> fromCurrencies = new ArrayList<>();
        List<String> toCurrencies = new ArrayList<>();
        List<Double> rates = new ArrayList<>();

        // Populate exchange rates
        for (Exchange exchange : exchangeRates) {
            fromCurrencies.add(exchange.getFrom());
            toCurrencies.add(exchange.getTo());
            rates.add(exchange.getRate());

            // Add the inverse rate
            fromCurrencies.add(exchange.getTo());
            toCurrencies.add(exchange.getFrom());
            rates.add(1 / exchange.getRate());
        }

        // Use a queue for BFS
        Queue<String> currentPath = new LinkedList<>();
        Queue<Double> currentRates = new LinkedList<>();

        // Initialize the first step
        currentPath.add(from);
        currentRates.add(1.0);
        visitedCurrencies.add(from);

        while (!currentPath.isEmpty()) {
            String current = currentPath.poll();
            Double currentRate = currentRates.poll();

            // Check if currentRate is null
            if (currentRate == null) {
                continue;
            }

            // Check if we have reached the destination
            if (current.equals(to)) {
                return currentRate;
            }

            // Check neighbors
            for (int i = 0; i < fromCurrencies.size(); i++) {
                if (fromCurrencies.get(i).equals(current)) {
                    String neighbor = toCurrencies.get(i);
                    if (!visitedCurrencies.contains(neighbor)) {
                        currentPath.add(neighbor);
                        currentRates.add(currentRate * rates.get(i));
                        visitedCurrencies.add(neighbor);
                    }
                }
            }
        }

        return 0.0;
    }
}
