package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Grant provided by a Foundation. Encapsulates logistical information about a grant and any UserFileUploads
 * related to the grant and/or its application process.
 */
@Entity(name = "foundation_grant")
public class Grant extends BasicEntity implements MonetaryContribution, Serializable {

    @Column
    @NotNull
    @JsonView({Views.GrantDetails.class, Views.GrantList.class})
    private String name;

    @Column
    @JsonView({Views.GrantDetails.class, Views.GrantList.class})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate startPeriod;

    @Column
    @JsonView({Views.GrantDetails.class, Views.GrantList.class})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate endPeriod;

    @Column
    @JsonView({Views.GrantDetails.class})
    private String restriction;

    @Column
    @JsonView({Views.GrantDetails.class})
    private String description;

    @Column
    @JsonView({Views.GrantDetails.class, Views.GrantList.class})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate intentDeadline;

    @Column
    @JsonView({Views.GrantDetails.class, Views.GrantList.class})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate applicationDeadline;

    @Column
    @JsonView({Views.GrantDetails.class, Views.GrantList.class})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate reportDeadline;

    @Column
    @JsonView({Views.GrantDetails.class, Views.GrantList.class})
    private int amountAppliedFor;

    @Column
    @JsonView({Views.GrantDetails.class, Views.GrantList.class})
    private int amountReceived;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "foundation_id")
    @NotNull
    @JsonView({Views.GrantDetails.class, Views.GrantList.class})
    private Foundation foundation;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "grant_id")
    @JsonView({Views.GrantDetails.class})
    private Set<UserFileUpload> fileUploads;

    public Grant() {
        super();
    }

    /**
     * @param grantName the name of the Grant
     * @param foundation the Foundation the Grant comes from
     */
    public Grant(String grantName, Foundation foundation) {
        super();
        this.name = grantName;
        this.foundation = foundation;
    }

    /**
     * @return the Foundation the Grant comes from
     */
    public Foundation getFoundation() {
        return foundation;
    }

    public void setFoundation(Foundation foundation) {
        this.foundation = foundation;
    }

    /**
     * @return the UserFileUploads related to the Grant
     */
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

    /**
     * @return the name of the Grant
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the start of the period covered by the Grant
     */
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

    /**
     * @return the end of the period covered by the Grant
     */
    public LocalDate getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(LocalDate endPeriod) {
        this.endPeriod = endPeriod;
    }

    /**
     * @return what, if anything, this grant is restricted to
     */
    public String getRestriction() {
        return restriction;
    }

    public void setRestriction(String restriction) {
        this.restriction = restriction;
    }

    /**
     * @return the description of the Grant
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the deadline for the Grant's letter of intent
     */
    public LocalDate getIntentDeadline() {
        return intentDeadline;
    }

    public void setIntentDeadline(LocalDate intentDeadline) {
        this.intentDeadline = intentDeadline;
    }

    /**
     * @return the deadline for the Grant application
     */
    public LocalDate getApplicationDeadline() {
        return applicationDeadline;
    }

    public void setApplicationDeadline(LocalDate applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    /**
     * @return the report deadline for the Grant
     */
    public LocalDate getReportDeadline() {
        return reportDeadline;
    }

    public void setReportDeadline(LocalDate reportDeadline) {
        this.reportDeadline = reportDeadline;
    }

    /**
     * @return the amount of money applied for in the Grant
     */
    public int getAmountAppliedFor() {
        return amountAppliedFor;
    }

    public void setAmountAppliedFor(int amountAppliedFor) {
        this.amountAppliedFor = amountAppliedFor;
    }

    /**
     * @return the amount of money received by the organization running Bayard under the Grant
     */
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

    @Override
    public int hashCode() {
        if (getId() != null) {
            return (getId() + getCreated()).hashCode();
        } else {
            if (getName() != null) {
                return (getName() + getCreated()).hashCode();
            }
            //TODO: only necessary as a workaround to the problematic hashCode of BasicEntity
            return getCreated().hashCode();
        }
    }
}
