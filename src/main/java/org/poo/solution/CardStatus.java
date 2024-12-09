package org.poo.solution;

/**
 * Interface for CardStatus
 */
public interface CardStatus {
    /**
     * Method to make the card frozen
     */
    void makeFrozen();
    /**
     * Method to make the card warning
     */
    void makeWarning();
    /**
     * Method to check if the card is frozen
     * @return true if the card is frozen, false otherwise
     */
    boolean ruFrozen();
}
