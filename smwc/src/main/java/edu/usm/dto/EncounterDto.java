package edu.usm.dto;

import edu.usm.domain.Encounter;

import java.io.Serializable;

public class EncounterDto extends BasicEntityDto implements Serializable {

    private String id;
    private ContactDto contact;
    private String encounterDate;
    private String notes;
    private ContactDto initiator;
    private int assessment;
    private String type;



    public Encounter convertToEncounter () {
        Encounter encounter;

        if (getId() == null) {
            encounter = new Encounter();
        } else {
            encounter = new Encounter(getId());
            encounter.setCreated(getCreated());
            encounter.setLastModified(getLastModified());

        }


        encounter.setType(getType());
        encounter.setEncounterDate(getEncounterDate());
        encounter.setNotes(getNotes());
        encounter.setAssessment(getAssessment());

        //TODO add contact, initiator, and form

        return encounter;

    }

    public ContactDto getContact() {
        return contact;
    }

    public void setContact(ContactDto contact) {
        this.contact = contact;
    }

    public String getEncounterDate() {
        return encounterDate;
    }

    public void setEncounterDate(String encounterDate) {
        this.encounterDate = encounterDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ContactDto getInitiator() {
        return initiator;
    }

    public void setInitiator(ContactDto initiator) {
        this.initiator = initiator;
    }

    public int getAssessment() {
        return assessment;
    }

    public void setAssessment(int assessment) {
        this.assessment = assessment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
