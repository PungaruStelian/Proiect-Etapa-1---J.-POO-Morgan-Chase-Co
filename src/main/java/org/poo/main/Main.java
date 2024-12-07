package org.poo.main;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.poo.checker.Checker;
import org.poo.checker.CheckerConstants;
import org.poo.fileio.ObjectInput;
import org.poo.solution.*;
import org.poo.solution.Object;
import org.poo.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

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

        Object object = Object.getInstance(inputData);
        Handle handle = Handle.getInstance();
        handle.createTransaction(object);

        Utils.resetRandom();

        // Process commands
        for (Command command : object.getCommands()) {
            ObjectNode result = objectMapper.createObjectNode();
            result.put("command", command.getCommand());

            switch (command.getCommand()) {

                case "printUsers":
                    handle.printUsers(object, command, result, output);
                    break;

                case "addAccount":
                    handle.addAccount(object, command);
                    break;

                case "deleteAccount":
                    handle.deleteAccount(object, command, result, output);
                    break;

                case "deleteCard":
                    handle.deleteCard(object, command);
                    break;

                case "createCard":
                    handle.createCard(object, command);
                    break;

                case "createOneTimeCard":
                    handle.createOneTimeCard(object, command);
                    break;

                case "addFunds":
                    handle.addFunds(object, command);
                    break;

                case "setMinimumBalance":
                    handle.setMinimumBalance(object, command);
                    break;

                case "payOnline":
                    handle.payOnline(object, command, result, output);
                    break;

                case "sendMoney":
                    handle.sendMoney(object, command);
                    break;

                case "changeInterestRate":
                    handle.changeInterestRate(object, command, result, output);
                    break;

                case "addInterest":
                    handle.addInterest(object, command, result, output);
                    break;

                case "setAlias":
                    handle.setAlias(object, command);
                    break;

                case "printTransactions":
                    handle.printTransactions(object, command, result, output);
                    break;

                case "checkCardStatus":
                    handle.checkCardStatus(object, command, result, output);
                    break;

                case "splitPayment":
                    handle.splitPayment(object, command, result, output);
                    break;

                case "report":
                    handle.report(object, command, result, output);
                    break;

                case "spendingsReport":
                    handle.spendingsReport(object, command, result, output);
                    break;

                case "breakpoint":
                    break;

                default:
                    result.put("error", "Invalid command: " + command.getCommand());
                    output.add(result);
                    break;
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
        String fileName = file.getName()
                .replaceAll(CheckerConstants.DIGIT_REGEX, CheckerConstants.EMPTY_STR);
        return Integer.parseInt(fileName.substring(0, 2));
    }
}
