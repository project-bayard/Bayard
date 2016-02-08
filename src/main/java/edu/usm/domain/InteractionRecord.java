package edu.usm.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

/**
 * Created by andrew on 1/24/16.
 */
@Entity(name="interaction_record")
public class InteractionRecord extends BasicEntity implements Serializable {

    @Column
    private String personContacted;

    @Column
    private LocalDate dateOfInteraction;

    @Column
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

    public Foundation getFoundation() {
        return foundation;
    }

    public void setFoundation(Foundation foundation) {
        this.foundation = foundation;
    }

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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<UserFileUpload> getFileUploads() {
        return fileUploads;
    }

    public void setFileUploads(Set<UserFileUpload> fileUploads) {
        this.fileUploads = fileUploads;
    }
}
