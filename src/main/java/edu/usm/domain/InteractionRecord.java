package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A record of an interaction between the organization running Bayard and a particular Foundation.
 */
@Entity(name="interaction_record")
public class InteractionRecord extends BasicEntity implements Serializable {

    @Column
    @NotNull
    @JsonView({Views.InteractionRecordDetails.class, Views.InteractionRecordList.class, Views.FoundationDetails.class})
    private String personContacted;

    @Column
    @NotNull
    @JsonView({Views.InteractionRecordDetails.class, Views.InteractionRecordList.class, Views.FoundationDetails.class})
    private LocalDate dateOfInteraction;

    @Column
    @NotNull
    @JsonView({Views.InteractionRecordDetails.class, Views.InteractionRecordList.class})
    private String interactionType;

    @Lob
    @Column
    @JsonView({Views.InteractionRecordDetails.class})
    private String notes;

    @Column
    @JsonView({Views.InteractionRecordDetails.class, Views.InteractionRecordList.class})
    private boolean requiresFollowUp;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @NotNull
    @JsonView({Views.InteractionRecordDetails.class, Views.InteractionRecordList.class})
    private Foundation foundation;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "interaction_record_id")
    @JsonView({Views.InteractionRecordDetails.class})
    private Set<UserFileUpload> fileUploads = new HashSet<>();

    public InteractionRecord() {
        super();
    }

    /**
     * @param personContacted the person contacted at the Foundation
     * @param dateOfInteraction when the interaction took place
     * @param interactionType the type of the interaction
     * @param foundation the Foundation
     */
    public InteractionRecord(String personContacted, LocalDate dateOfInteraction, String interactionType, Foundation foundation) {
        super();
        this.foundation = foundation;
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
     * @return true if this interaction requires a follow-up by the organization running Bayard
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
     * @return UserFileUploads pertaining to the interaction
     */
    public Set<UserFileUpload> getFileUploads() {
        return fileUploads;
    }

    public void setFileUploads(Set<UserFileUpload> fileUploads) {
        this.fileUploads = fileUploads;
    }

    @Override
    public int hashCode() {
        //TODO: only necessary as a workaround to the problematic hashCode of BasicEntity
        if (getId() != null) {
            return (getId() + getCreated()).hashCode();
        } else {
            if (getPersonContacted() != null && getDateOfInteraction() != null) {
                return (getPersonContacted() + getDateOfInteraction().toString() + getCreated()).hashCode();
            }
            return getCreated().hashCode();
        }
    }

}
