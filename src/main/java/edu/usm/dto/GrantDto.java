package edu.usm.dto;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by andrew on 2/18/16.
 */
public class GrantDto implements Serializable {

    private String foundationId;

    private String name;

    private LocalDate startPeriod;

    private LocalDate endPeriod;

    private String restriction;

    private String description;

    private LocalDate intentDeadline;

    private LocalDate applicationDeadline;

    private LocalDate reportDeadline;

    private int amountAppliedFor;

    private int amountReceived;

    public String getFoundationId() {
        return foundationId;
    }

    public void setFoundationId(String foundationId) {
        this.foundationId = foundationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(LocalDate startPeriod) {
        this.startPeriod = startPeriod;
    }

    public LocalDate getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(LocalDate endPeriod) {
        this.endPeriod = endPeriod;
    }

    public String getRestriction() {
        return restriction;
    }

    public void setRestriction(String restriction) {
        this.restriction = restriction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getIntentDeadline() {
        return intentDeadline;
    }

    public void setIntentDeadline(LocalDate intentDeadline) {
        this.intentDeadline = intentDeadline;
    }

    public LocalDate getApplicationDeadline() {
        return applicationDeadline;
    }

    public void setApplicationDeadline(LocalDate applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    public LocalDate getReportDeadline() {
        return reportDeadline;
    }

    public void setReportDeadline(LocalDate reportDeadline) {
        this.reportDeadline = reportDeadline;
    }

    public int getAmountAppliedFor() {
        return amountAppliedFor;
    }

    public void setAmountAppliedFor(int amountAppliedFor) {
        this.amountAppliedFor = amountAppliedFor;
    }

    public int getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(int amountReceived) {
        this.amountReceived = amountReceived;
    }
}
