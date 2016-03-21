package edu.usm.dto;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by andrew on 3/17/16.
 */
public class DonationDto implements Serializable {

    private int amount;

    private String method;

    private LocalDate dateOfReceipt;

    private LocalDate dateOfDeposit;

    private String restrictedToCategory;

    private String budgetItemId;

    private boolean anonymous;

    private boolean standalone;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public LocalDate getDateOfReceipt() {
        return dateOfReceipt;
    }

    public void setDateOfReceipt(LocalDate dateOfReceipt) {
        this.dateOfReceipt = dateOfReceipt;
    }

    public LocalDate getDateOfDeposit() {
        return dateOfDeposit;
    }

    public void setDateOfDeposit(LocalDate dateOfDeposit) {
        this.dateOfDeposit = dateOfDeposit;
    }

    public String getRestrictedToCategory() {
        return restrictedToCategory;
    }

    public void setRestrictedToCategory(String restrictedToCategory) {
        this.restrictedToCategory = restrictedToCategory;
    }

    public String getBudgetItemId() {
        return budgetItemId;
    }

    public void setBudgetItemId(String budgetItemId) {
        this.budgetItemId = budgetItemId;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public boolean isStandalone() {
        return standalone;
    }

    public void setStandalone(boolean standalone) {
        this.standalone = standalone;
    }
}
