package org.poo.main;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.poo.checker.Checker;
import org.poo.checker.CheckerConstants;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;
import org.poo.utils.Utils;

import  org.poo.solution.Object;
import  org.poo.solution.User;
import  org.poo.solution.Command;
import  org.poo.solution.Exchange;
import  org.poo.solution.Commerciant;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The entry point to this homework. It runs the checker that tests your implementation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        var sortedFiles = Arrays.stream(Objects.requireNonNull(directory.listFiles())).
                sorted(Comparator.comparingInt(Main::fileConsumer))
                .toList();

        for (File file : sortedFiles) {
            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(CheckerConstants.TESTS_PATH + filePath1);
        ObjectInput inputData = objectMapper.readValue(file, ObjectInput.class);

        ArrayNode output = objectMapper.createArrayNode();

        //TODO Implement your function here

        // Create the Object instance from the solution package
        Object object = new Object(inputData);

        // Initialize userAccounts map
        Map<String, List<ObjectNode>> userAccounts = new HashMap<>();
        for (User user : object.getUsers()) {
            userAccounts.put(user.getEmail(), new ArrayList<>());
        }

        // Process commands
        for (Command command : object.getCommands()) {
            ObjectNode result = objectMapper.createObjectNode();
            result.put("command", command.getCommand());
            switch (command.getCommand()) {
                case "printUsers":
                    ArrayNode usersArray = objectMapper.createArrayNode();
                    for (User user : object.getUsers()) {
                        ObjectNode userNode = objectMapper.createObjectNode()
                                .put("firstName", user.getFirstName())
                                .put("lastName", user.getLastName())
                                .put("email", user.getEmail());
                        ArrayNode accountsArray = objectMapper.createArrayNode();
                        for (ObjectNode account : userAccounts.get(user.getEmail())) {
                            accountsArray.add(account);
                        }
                        userNode.set("accounts", accountsArray);
                        usersArray.add(userNode);
                    }
                    result.set("output", usersArray);
                    break;
                case "addAccount":
                    if (!userAccounts.containsKey(command.getEmail())) {
                        userAccounts.put(command.getEmail(), new ArrayList<>());
                    }
                    result.remove("command");
                    result.put("IBAN", Utils.generateIBAN());
                    result.put("balance", 0.0);
                    result.put("currency", command.getCurrency());
                    result.put("type", command.getAccountType());
                    ArrayNode cardsArray = objectMapper.createArrayNode();
                    result.set("cards", cardsArray);
                    userAccounts.get(command.getEmail()).add(result);
                    break;
                case "createCard":
                    if (userAccounts.containsKey(command.getEmail())) {
                        for (ObjectNode account : userAccounts.get(command.getEmail())) {
                            if (account.get("IBAN").asText().equals(command.getAccount())) {
                                ArrayNode cardssArray = (ArrayNode) account.get("cards");
                                ObjectNode cardNode = objectMapper.createObjectNode()
                                        .put("cardNumber", Utils.generateCardNumber())
                                        .put("status", "active");
                                cardssArray.add(cardNode);
                            }
                        }
                    }
                    break;
                case "addFunds":
                    if (userAccounts.containsKey(command.getEmail())) {
                        for (ObjectNode account : userAccounts.get(command.getEmail())) {
                            if (account.get("IBAN").asText().equals(command.getAccount())) {
                                double newBalance = account.get("balance").asDouble() + command.getAmount();
                                account.put("balance", newBalance);
                            }
                        }
                    }
                    break;
                default:
                    // Handle other commands if necessary
                    break;
            }
            if(!command.getCommand().equals("addAccount") && !command.getCommand().equals("createCard") && !command.getCommand().equals("addFunds")) {
                result.put("timestamp", command.getTimestamp());
                output.add(result);
            }
        }

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }

    /**
     * Method used for extracting the test number from the file name.
     *
     * @param file the input file
     * @return the extracted numbers
     */
    public static int fileConsumer(final File file) {
        return Integer.parseInt(
                file.getName()
                        .replaceAll(CheckerConstants.DIGIT_REGEX, CheckerConstants.EMPTY_STR)
        );
    }
}
