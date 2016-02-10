package edu.usm.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

/**
 * A record of an interaction between the organization running Bayard and a particular Foundation.
 */
@Entity(name="interaction_record")
public class InteractionRecord extends BasicEntity implements Serializable {

    @Column
    @NotNull
    private String personContacted;

    @Column
    @NotNull
    private LocalDate dateOfInteraction;

    @Column
    @NotNull
    private String interactionType;

    @Lob
    @Column
    private String notes;

    @Column
    private boolean requiresFollowUp;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private Foundation foundation;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "interaction_record_id")
    private Set<UserFileUpload> fileUploads;

    public InteractionRecord() {
        super();
    }

    /**
     * @param personContacted the person contacted at the Foundation
     * @param dateOfInteraction when the interaction took place
     * @param interactionType the type of the interaction
     */
    public InteractionRecord(String personContacted, LocalDate dateOfInteraction, String interactionType) {
        super();
        this.personContacted = personContacted;
        this.dateOfInteraction = dateOfInteraction;
        this.interactionType = interactionType;
    }

    public Foundation getFoundation() {
        return foundation;
    }

    public void setFoundation(Foundation foundation) {
        this.foundation = foundation;
    }

    /**
     * @return true if this interaction requires a follow up by the organization running Bayard
     */
    public boolean isRequiresFollowUp() {
        return requiresFollowUp;
    }

    public void setRequiresFollowUp(boolean requiresFollowUp) {
        this.requiresFollowUp = requiresFollowUp;
    }

    public String getPersonContacted() {
        return personContacted;
    }

    public void setPersonContacted(String personContacted) {
        this.personContacted = personContacted;
    }

    public LocalDate getDateOfInteraction() {
        return dateOfInteraction;
    }

    public void setDateOfInteraction(LocalDate dateOfInteraction) {
        this.dateOfInteraction = dateOfInteraction;
    }

    public String getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }

    /**
     * @return notes pertaining to the interaction
     */
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * @return UserFileUploads pertaining to this interaction
     */
    public Set<UserFileUpload> getFileUploads() {
        return fileUploads;
    }

    public void setFileUploads(Set<UserFileUpload> fileUploads) {
        this.fileUploads = fileUploads;
    }
}
