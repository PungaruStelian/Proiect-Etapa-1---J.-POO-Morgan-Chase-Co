# Project Assignment POO - J. POO Morgan - Phase One

![](https://s.yimg.com/ny/api/res/1.2/aN0SfZTtLF5hLNO0wIN3gg--/YXBwaWQ9aGlnaGxhbmRlcjt3PTcwNTtoPTQyNztjZj13ZWJw/https://o.aolcdn.com/hss/storage/midas/b23d8b7f62a50a7b79152996890aa052/204855412/fit.gif)

#### Assignment Link: [https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/2024/proiect-e1](https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/2024/proiect-e1)

## Overview

This project is a simulation of a banking system that handles various operations such as creating accounts, managing cards, processing transactions, and generating reports. The system is implemented in Java and uses JSON files for input and output.

## Singleton Design
### `Handle`
- **Description**: Implements the Singleton design pattern, ensuring that only one instance of the class exists.
- **Key Components**:
    - `INSTANCE`: A static final instance of the class.
    - `Handle()`: Private constructor to prevent direct instantiation.
    - `getInstance()`: Static method to retrieve the Singleton instance.
    - `objectMapper`: An `ObjectMapper` instance for JSON processing.

### `Object`

- **Description**: Represents a generic object with a unique identifier.
- **Key Components**:
    - `INSTANCE`: A static final instance of the class.
    - `Object()`: Private constructor to prevent direct instantiation.
    - `getInstance()`: Static method to retrieve the Singleton instance.
    - `Commerciants`: A list of all the merchants that have been used in transactions.
    - `Commands`: A list of all the commands that have been used.
    - `ExchangeRates`: A list of all the exchange rates that have been used.

---

## Builder Design

### PermanentCardBuilder

- **Description**: Implements the Builder design pattern for creating permanent cards.
- **Key Components**:
    - `setCardNumber()`: Sets the card number.
    - `setStatus()`: Sets the card status.
    - `build()`: Constructs the card.

### OneTimeCardBuilder

- **Description**: Implements the Builder design pattern for creating one-time cards.
- **Key Components**:
    - `CardBuilder`: An instance of `PermanentCardBuilder` for creating the card.
    - `setCardNumber()`: Sets the card number.
    - `setStatus()`: Sets the card status.
    - `build()`: Constructs the card.

---

## Abstract Class
### `AbstractCard`
- **Description**: Represents a generic card with a card number and status.
- **Key Components**:
  - `cardNumber`: The card number.
  - `status`: The card status.
  - `ruFrozen`: A boolean indicating if the card is frozen.
  - `makeFrozen()`: Freezes the card.
  - `makeWarning()`: Sets the card status to warning.
  - `ruUsed()`: A boolean indicating if the card has been used.
  - `makeAsUsed()`: Sets the card as used.

## Interface
### `AccountInterface`
- **Description**: Defines methods for account operations. Getters and setters for an normal functionality account and aditional methods for a different types of account.

---

## Handle's Private Methods 
### `handleNonSavingsAccount`
- **Purpose**: Handles operations that fail due to non-savings account restrictions.
- **Parameters**:
    - `otherObjectMapper`: Another `ObjectMapper` instance for creating JSON nodes.
    - `result`, `output`: JSON nodes to store results.
    - `command`: Command metadata for the operation.
- **Operation**: Creates a JSON error message indicating the account is not a savings account.

### `addTransaction`
- **Purpose**: Records a transaction in a user's account history.
- **Parameters**:
    - Various fields describing the transaction (e.g., `user`, `command`, `amount`, `currency`).
- **Operation**: Constructs and stores a JSON node representing the transaction.

### `addOutput`
- **Purpose**: Appends results to the output JSON array.
- **Parameters**:
    - `result`: The root JSON object.
    - `output`: The JSON array to append the result.
    - `command`: Metadata for the operation.
    - `arrayNode`, `objectNode`: The data to be added.
- **Operation**: Modifies the `result` with either an array or object output, adding it to the provided output array.

---

## Public Methods
### `createTransaction`
- **Purpose**: Initializes empty transaction logs for all users.
- **Parameters**:
    - `object`: The main data container.
- **Operation**: Resets each user's transaction list to an empty array.

### `printUsers`
- **Purpose**: Outputs a list of all users as JSON.
- **Parameters**:
    - `object`: Main data container.
    - `command`, `result`, `output`: JSON-related parameters.
- **Operation**: Adds JSON representations of all users to the output.

### `addAccount`
- **Purpose**: Adds a new account for a user.
- **Parameters**:
    - `object`: Main data container.
    - `command`: Command containing details for the account.
- **Operation**: Creates an account, assigns it to the user, and logs the transaction.

### `deleteAccount`
- **Purpose**: Deletes a user account if its balance is zero.
- **Parameters**:
    - Similar to `addAccount`.
- **Operation**: Verifies balance, removes the account, and logs the action or error.

### `createCard`
- **Purpose**: Creates a new permanent card linked to an account.
- **Parameters**:
    - Similar to `addAccount`.
- **Operation**: Adds a new card to the account and logs the creation.

### `addFunds`
- **Purpose**: Adds funds to a user's account.
- **Parameters**:
    - Similar to `addAccount`.
- **Operation**: Adjusts the account balance by the specified amount.

### `deleteCard`
- **Purpose**: Deletes a card associated with an account.
- **Parameters**:
    - Similar to `addAccount`.
- **Operation**: Removes the card from the account and logs the deletion.

### `createOneTimeCard`
- **Purpose**: Creates a single-use card.
- **Parameters**:
    - Similar to `addAccount`.
- **Operation**: Creates a disposable card, attaches it to the account, and logs the transaction.

### `setMinimumBalance`
- **Purpose**: Sets a minimum balance for an account.
- **Parameters**:
    - Similar to `addAccount`.
- **Operation**: Updates the account's minimum balance.

### `payOnline`
- **Purpose**: Processes an online payment.
- **Parameters**:
    - Similar to `addAccount`.
- **Operation**: Handles payment logic, including currency conversion and card status checks.

### `sendMoney`
- **Purpose**: Transfers money between accounts.
- **Parameters**:
    - Similar to `addAccount`.
- **Operation**: Executes the transfer, adjusting balances and logging transactions.

### `setAlias`
- **Purpose**: Assigns an alias to an account.
- **Parameters**:
    - Similar to `addAccount`.
- **Operation**: Updates the account's alias field.

### `printTransactions`
- **Purpose**: Filters and displays a user's transactions.
- **Parameters**:
    - Similar to `printUsers`.
- **Operation**: Retrieves transactions based on a timestamp filter.

### `checkCardStatus`
- **Purpose**: Verifies the status of a card.
- **Parameters**:
    - Similar to `addAccount`.
- **Operation**: Logs warnings or errors related to card usage.

### `splitPayment`
- **Purpose**: Divides a payment among multiple accounts.
- **Parameters**:
    - Similar to `addAccount`.
- **Operation**: Calculates and processes each participant's share.

### `changeInterestRate`
- **Purpose**: Updates the interest rate of a savings account.
- **Parameters**:
    - Similar to `addAccount`.
- **Operation**: Validates the account type and applies the new rate.

### `addInterest`
- **Purpose**: Adds accrued interest to a savings account.
- **Parameters**:
    - Similar to `addAccount`.
- **Operation**: Calculates and credits interest to the account.

### `report`
- **Purpose**: Generates a report for an account's activity.
- **Parameters**:
    - Similar to `printTransactions`.
- **Operation**: Filters and structures transaction data into a detailed report.

### `spendingsReport`
- **Purpose**: Summarizes expenditures by merchant.
- **Parameters**:
    - Similar to `report`.
- **Operation**: Aggregates transaction data, sorting by total spend per merchant.

---

## Dependencies
- **Packages**:
    - `org.poo.utils.Utils`: Utility class for auxiliary functions.
    - `com.fasterxml.jackson.databind`: For JSON manipulation.

## Notes
- I didn't use oop concepts at the maximum level because I wanted to try as many as possible, and I might need them more in the second part of the project. However, I followed almost all the indications for solving the requirement in order not to be de-pointed for not using the oop concepts correctly.
  With the help of this theme, I became more experienced in programming in java

---