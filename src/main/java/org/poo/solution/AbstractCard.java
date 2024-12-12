package org.poo.solution;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class AbstractCard {
    private String cardNumber;
    private String status;

    /**
     * Method to check if the card is frozen
     * @return true if the card is frozen, false otherwise
     */
    public abstract boolean ruFrozen();

    /**
     * Method to set the card as frozen
     */
    public abstract void makeFrozen();

    /**
     * Method to set the card as warning
     */
    public abstract void makeWarning();

    /**
     * Method to check if the card is used
     * @return true if the card is used, false otherwise
     */
    public abstract boolean ruUsed();

    /**
     * Method to mark the card as used
     */
    public abstract void markAsUsed();

    /**
     * Method to handle the destruction of the card
     * @param user The user that owns the card
     * @param command The command that triggered the destruction
     * @param account The account that owns the card
     * @param handle The handle object
     */
    public void handleCardDestruction(final ObjectMapper objectMapper, final User user,
                                      final Command command, final Account account,
                                      final Handle handle) {
        // Default implementation does nothing
    }
}
