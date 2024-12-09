package org.poo.solution;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.poo.fileio.CommerciantInput;

import java.util.List;

@Data
@NoArgsConstructor
public class Commerciant {
    private int id;
    private String description;
    private List<String> commerciants;

    /**
     * Constructor for Commerciant
     * @param commerciantInput The CommerciantInput object
     */
    public Commerciant(final CommerciantInput commerciantInput) {
        this.id = commerciantInput.getId();
        this.description = commerciantInput.getDescription();
        this.commerciants = commerciantInput.getCommerciants();
    }
}
