package edu.usm.domain;

import java.time.LocalDate;

/**
 * Created by andrew on 1/25/16.
 */
public interface MonetaryContribution {

    /**
     * @return the amount of the contribution
     */
    int getAmount();

    /**
     * @return the date the contribution was received
     */
    LocalDate getDateOfReceipt();

}
