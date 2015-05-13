package edu.usm.dto;

import edu.usm.domain.Encounter;
import edu.usm.domain.EncounterType;

import java.io.Serializable;

public class EncounterDto implements Serializable {

    private ContactDto contact;
    private String date;
    private String notes;
    private ContactDto initiator;
    private int assessment;
    private EncounterType type;



    public Encounter convertToEncounter () {
        return new Encounter();
    }

    public ContactDto getContact() {
        return contact;
    }

    public void setContact(ContactDto contact) {
        this.contact = contact;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public EncounterType getType() {
        return type;
    }

    public void setType(EncounterType type) {
        this.type = type;
    }
}
