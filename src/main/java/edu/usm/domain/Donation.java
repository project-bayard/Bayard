package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a monetary contribution given to the organization running Bayard.
 */
@Entity(name = "donation")
public class Donation extends BasicEntity implements MonetaryContribution, Serializable {

    @Column
    @JsonView(Views.DonationDetails.class)
    private int amount;

    @Column
    @JsonView(Views.DonationDetails.class)
    private String method;

    @Column
    @JsonView(Views.DonationDetails.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate dateOfReceipt;

    @Column
    @JsonView(Views.DonationDetails.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate dateOfDeposit;

    @Column
    @JsonView(Views.DonationDetails.class)
    private String restrictedToCategory;

    @Column
    @JsonView(Views.DonationDetails.class)
    private String budgetItem;

    @Column
    @JsonView(Views.DonationDetails.class)
    private boolean anonymous;

    @Column
    @JsonView(Views.DonationDetails.class)
    private boolean standalone;

    public Donation() {
        super();
    }

    /**
     * @param amount the amount of money donated
     * @param method the method by which this donation was made
     *
     * @param dateOfReceipt the date the donation was received
     * @param dateOfDeposit the date the donation was deposited
     */
    public Donation(int amount, String method, LocalDate dateOfReceipt, LocalDate dateOfDeposit) {
        super();
        this.amount = amount;
        this.method = method;
        this.dateOfReceipt = dateOfReceipt;
        this.dateOfDeposit = dateOfDeposit;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * @return the method by which this Donation was made
     */
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
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

    /**
     * @return the category to which this Donation is restricted
     */
    public String getRestrictedToCategory() {
        return restrictedToCategory;
    }

    public void setRestrictedToCategory(String restrictedToCategory) {
        this.restrictedToCategory = restrictedToCategory;
    }

    /**
     * @return whether or not this Donation is restricted to a certain category
     */
    public boolean isRestricted() {
        return !(this.getRestrictedToCategory() == null) && !(this.getRestrictedToCategory().isEmpty());
    }

    /**
     * @return the budget item this Donation is associated with
     */
    public String getBudgetItem() {
        return budgetItem;
    }

    public void setBudgetItem(String budgetItem) {
        this.budgetItem = budgetItem;
    }

    /**
     * @return whether or not this Donation is associated with a particular budget item
     */
    public boolean isForBudgetItem() {
        return !(this.budgetItem == null) && !(this.budgetItem.isEmpty());
    }

    /**
     * @return whether or not the Donation is intended to be from an anonymous source
     */
    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    /**
     * @return true if this Donation is unrelated to a particular BasicEntity
     */
    public boolean isStandalone() {
        return standalone;
    }

    public void setStandalone(boolean standalone) {
        this.standalone = standalone;
    }
}