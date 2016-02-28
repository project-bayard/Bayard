package edu.usm.domain;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

/**
 * A contiguous period of time over which a Contact has made monthly monetary contributions.
 */
@Entity(name = "sustainer_period")
public class SustainerPeriod extends BasicEntity implements MonetaryContribution, Comparable<SustainerPeriod>, Serializable {

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @NotNull
    @JsonView(Views.SustainerPeriodDetails.class)
    private DonorInfo donorInfo;

    @Column
    @NotNull
    @JsonView(Views.SustainerPeriodDetails.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)

    private LocalDate periodStartDate;

    @Column
    @JsonView(Views.SustainerPeriodDetails.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate cancelDate;

    @Column
    @JsonView(Views.SustainerPeriodDetails.class)
    private int monthlyAmount;

    @Column
    @JsonView(Views.SustainerPeriodDetails.class)
    private boolean sentIRSLetter;

    public SustainerPeriod() {
        super();
    }

    /**
     * @param donorInfo the DonorInfo of the sustaining Contact
     * @param periodStartDate the start of the sustaining period
     * @param monthlyAmount the monthly amount given over the sustaining period
     */
    public SustainerPeriod(DonorInfo donorInfo, LocalDate periodStartDate, int monthlyAmount) {
        super();
        this.donorInfo = donorInfo;
        this.periodStartDate = periodStartDate;
        this.monthlyAmount = monthlyAmount;
    }

    @Override
    public int compareTo(SustainerPeriod o) {
        if (o.getPeriodStartDate().isEqual(this.getPeriodStartDate())) {
            return 0;
        }
        return (this.getPeriodStartDate().isBefore(o.getPeriodStartDate())) ? 1 : -1;
    }

    /*Calculates the total contributions represented by this SustainerPeriod. The difference between periodStartDate
    * and cancelDate is used to determine the months of active contribution. If the period has not been canceled, the
    * calculation is performed relative to the current month.*/
    public int getTotalYearToDate() {

        LocalDate endOfPeriod = this.getCancelDate();
        if (null == endOfPeriod) {
            endOfPeriod = LocalDate.now();
        }

        Period periodBetween = Period.between(this.getPeriodStartDate(), endOfPeriod);
        if (periodBetween.isNegative()) {
            return 0;
        }
        int monthsBetween = (int)periodBetween.toTotalMonths();

        /*Round any additional days into an entire month*/
        if (periodBetween.getDays() > 0) {
            monthsBetween++;
        }

        return monthsBetween * this.getMonthlyAmount();

    }

    @Override
    public int getAmount() {
        return getTotalYearToDate();
    }

    @Override
    public LocalDate getDateOfReceipt() {
        return this.getPeriodStartDate();
    }

    public DonorInfo getDonorInfo() {
        return donorInfo;
    }

    public LocalDate getPeriodStartDate() {
        return periodStartDate;
    }

    public void setPeriodStartDate(LocalDate periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    /**
     * @return the cancellation date of the sustaining period
     */
    public LocalDate getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    public void setDonorInfo(DonorInfo donorInfo) {
        this.donorInfo = donorInfo;
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
