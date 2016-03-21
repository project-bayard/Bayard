package edu.usm.dto;


import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by andrew on 2/19/16.
 */
public class InteractionRecordDto implements Serializable {

    private String personContacted;

    private LocalDate dateOfInteraction;

    private String interactionTypeId;

    private String notes;

    private boolean requiresFollowUp;

    private String foundationId;

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

    public String getInteractionTypeId() {
        return interactionTypeId;
    }

    public void setInteractionTypeId(String interactionTypeId) {
        this.interactionTypeId = interactionTypeId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isRequiresFollowUp() {
        return requiresFollowUp;
    }

    public void setRequiresFollowUp(boolean requiresFollowUp) {
        this.requiresFollowUp = requiresFollowUp;
    }

    public String getFoundationId() {
        return foundationId;
    }

    public void setFoundationId(String foundationId) {
        this.foundationId = foundationId;
    }
}
