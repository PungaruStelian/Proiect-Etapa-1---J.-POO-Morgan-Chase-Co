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
import org.poo.solution.*;
import org.poo.solution.Object;
import org.poo.utils.Utils;

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

        Utils.resetRandom();

        // Process commands
        for (Command command : object.getCommands()) {
            ObjectNode result = objectMapper.createObjectNode();
            result.put("command", command.getCommand());

            switch (command.getCommand()) {

                case "printUsers":
                    ArrayNode usersArray = objectMapper.createArrayNode();
                    for (User user : object.getUsers()) {
                        ObjectNode userNode = objectMapper.valueToTree(user);
                        usersArray.add(userNode);
                    }
                    result.set("output", usersArray);
                    break;

                case "addAccount":
                    for (User user : object.getUsers()) {
                        if (user.getEmail().equals(command.getEmail())) {
                            Account newAccount = new Account();
                            newAccount.setIBAN(Utils.generateIBAN());
                            newAccount.setBalance(0.0);
                            newAccount.setCurrency(command.getCurrency());
                            newAccount.setType(command.getAccountType());
                            newAccount.activate();
                            user.getAccounts().add(newAccount);
                        }
                    }
                    break;

                case "deleteAccount":
                    boolean ok = false;
                    for (User user : object.getUsers()) {
                        var accountIterator = user.getAccounts().iterator();
                        while (accountIterator.hasNext()) {
                            Account account = accountIterator.next();
                            if (account.getIBAN().equals(command.getAccount()) && account.getBalance() == 0) {
                                ok = true;
                                account.setStatus("closed");
                                result.remove("command");
                                result.put("success", "Account deleted");
                                result.put("timestamp", command.getTimestamp());
                                output.add(result);
                            }
                        }
                    }
                    if (!ok) {
                        result.put("description", "User not found");
                        result.put("timestamp", command.getTimestamp());
                        output.add(result);
                    }
                    break;

                case "createCard":
                    for (User user : object.getUsers()) {
                        if (user.getEmail().equals(command.getEmail())) {
                            for (Account account : user.getAccounts()) {
                                if (account.getIBAN().equals(command.getAccount())) {
                                    Cards newCard = new Cards();
                                    newCard.setCardNumber(Utils.generateCardNumber());
                                    newCard.activate();
                                    account.getCards().add(newCard);
                                }
                            }
                        }
                    }
                    break;

                case "addFunds":
                    for (User user : object.getUsers()) {
                        for (Account account : user.getAccounts()) {
                            if (account.getIBAN().equals(command.getAccount())) {
                                account.addFunds(command.getAmount());
                            }
                        }
                    }
                    break;

                case "breakpoint":
                    break;

                default:
                    result.put("error", "Invalid command: " + command.getCommand());
                    break;
            }
            if(!command.getCommand().equals("addAccount")
                    && !command.getCommand().equals("createCard")
                    && !command.getCommand().equals("addFunds")
                    && !command.getCommand().equals("deleteAccount")) {
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
