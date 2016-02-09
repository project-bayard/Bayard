package edu.usm.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by andrew on 1/24/16.
 */
@Entity(name = "foundation_grant")
public class Grant extends BasicEntity implements MonetaryContribution, Serializable {

    @Column
    @NotNull
    private String name;

    @Column
    private LocalDate startPeriod;

    @Column
    private LocalDate endPeriod;

    @Column
    private String restriction;

    @Column
    private String description;

    @Column
    private LocalDate intentDeadline;

    @Column
    private LocalDate applicationDeadline;

    @Column
    private LocalDate reportDeadline;

    @Column
    private int amountAppliedFor;

    @Column
    private int amountReceived;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "foundation_id")
    private Foundation foundation;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "grant_id")
    private Set<UserFileUpload> fileUploads;

    public Foundation getFoundation() {
        return foundation;
    }

    public void setFoundation(Foundation foundation) {
        this.foundation = foundation;
    }

    public Set<UserFileUpload> getFileUploads() {
        if (null == fileUploads) {
            fileUploads = new HashSet<>();
        }
        return fileUploads;
    }

    public void setFileUploads(Set<UserFileUpload> fileUploads) {
        this.fileUploads = fileUploads;
    }

    public void addFileUpload(UserFileUpload fileUpload) {
        this.getFileUploads().add(fileUpload);
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

    @Override
    public LocalDate getDateOfReceipt() {
        return getStartPeriod();
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

    @Override
    public int getAmount() {
        return getAmountReceived();
    }

    public void setAmountReceived(int amountReceived) {
        this.amountReceived = amountReceived;
    }
}
