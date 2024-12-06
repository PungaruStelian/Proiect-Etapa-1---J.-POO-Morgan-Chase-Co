package org.poo.solution;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.poo.fileio.CommandInput;

import java.util.List;

@Data
@NoArgsConstructor
public class Command {
    private List<String> accounts;
    private String command;
    private String email;
    private String account;
    private String currency;
    private String target;
    private String description;
    private String cardNumber;
    private String commerciant;
    private String receiver;
    private String alias;
    private String accountType;
    private double amount;
    private double minBalance;
    private int timestamp;
    private int startTimestamp;
    private int endTimestamp;
    private double interestRate;

    public Command(CommandInput commandInput) {
        this.command = commandInput.getCommand();
        this.email = commandInput.getEmail();
        this.account = commandInput.getAccount();
        this.currency = commandInput.getCurrency();
        this.amount = commandInput.getAmount();
        this.minBalance = commandInput.getMinBalance();
        this.target = commandInput.getTarget();
        this.description = commandInput.getDescription();
        this.cardNumber = commandInput.getCardNumber();
        this.commerciant = commandInput.getCommerciant();
        this.timestamp = commandInput.getTimestamp();
        this.startTimestamp = commandInput.getStartTimestamp();
        this.endTimestamp = commandInput.getEndTimestamp();
        this.receiver = commandInput.getReceiver();
        this.alias = commandInput.getAlias();
        this.accountType = commandInput.getAccountType();
        this.interestRate = commandInput.getInterestRate();
        this.accounts = commandInput.getAccounts();
    }
}