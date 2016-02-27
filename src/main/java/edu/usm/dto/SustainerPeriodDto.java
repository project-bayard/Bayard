package edu.usm.dto;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by andrew on 2/26/16.
 */
public class SustainerPeriodDto implements Serializable {

    private LocalDate periodStartDate;

    private LocalDate cancelDate;

    private int monthlyAmount;

    private boolean sentIRSLetter;

    public LocalDate getPeriodStartDate() {
        return periodStartDate;
    }

    public void setPeriodStartDate(LocalDate periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    public LocalDate getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    public int getMonthlyAmount() {
        return monthlyAmount;
    }

    public void setMonthlyAmount(int monthlyAmount) {
        this.monthlyAmount = monthlyAmount;
    }

    public boolean isSentIRSLetter() {
        return sentIRSLetter;
    }

    public void setSentIRSLetter(boolean sentIRSLetter) {
        this.sentIRSLetter = sentIRSLetter;
    }
}
