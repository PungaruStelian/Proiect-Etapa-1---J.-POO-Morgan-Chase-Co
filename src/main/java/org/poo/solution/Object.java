package org.poo.solution;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.poo.fileio.ObjectInput;
import java.util.*;

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
        Map<String, Map<String, Double>> graph = new HashMap<>();
        for (Exchange exchange : exchangeRates) {
            graph.putIfAbsent(exchange.getFrom(), new HashMap<>());
            graph.putIfAbsent(exchange.getTo(), new HashMap<>());
            graph.get(exchange.getFrom()).put(exchange.getTo(), exchange.getRate());
            graph.get(exchange.getTo()).put(exchange.getFrom(), 1 / exchange.getRate());
        }
        Queue<String> queue = new LinkedList<>();
        Queue<Double> rates = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.add(from);
        rates.add(1.0);
        visited.add(from);
        while (!queue.isEmpty()) {
            String current = queue.poll();
            Double currentRate = rates.poll();
            if (currentRate == null) {
                continue;
            }
            if (current.equals(to)) {
                return currentRate;
            }
            if (graph.containsKey(current)) {
                for (Map.Entry<String, Double> neighbor : graph.get(current).entrySet()) {
                    if (!visited.contains(neighbor.getKey())) {
                        queue.add(neighbor.getKey());
                        rates.add(currentRate * neighbor.getValue());
                        visited.add(neighbor.getKey());
                    }
                }
            }
        }
        return 0.0;
    }
}
