package edu.usm.domain;

import java.time.LocalDate;

/**
 * Created by andrew on 1/25/16.
 */
public interface MonetaryContribution {

    int getAmount();
    LocalDate getDateOfReceipt();

}
