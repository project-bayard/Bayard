package edu.usm.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDate;

@Entity(name = "donation")
public class Donation extends BasicEntity implements MonetaryContribution, Serializable {

    @Column
    private int amount;

    @Column
    private String type;

    @Column
    private LocalDate dateOfReceipt;

    @Column
    private LocalDate dateOfDeposit;

    @Column
    private String restrictedToCategory;

    @Column
    private String budgetItem;

    @Column
    private boolean anonymous;

    @Column
    private boolean standalone;

    @Override
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
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

    public boolean isRestricted() {
        return !(this.getRestrictedToCategory() == null) && !(this.getRestrictedToCategory().isEmpty());
    }

    public String getBudgetItem() {
        return budgetItem;
    }

    public void setBudgetItem(String budgetItem) {
        this.budgetItem = budgetItem;
    }

    public boolean isForBudgetItem() {
        return !(this.budgetItem == null) && !(this.budgetItem.isEmpty());
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