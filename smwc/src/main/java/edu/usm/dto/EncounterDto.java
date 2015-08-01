package edu.usm.dto;

import java.io.Serializable;

public class EncounterDto implements Serializable {

    private String encounterDate;
    private String notes;
    private String initiatorId;
    private int assessment;
    private String type;

    public String getEncounterDate() {
        return encounterDate;
    }

    public void setEncounterDate(String date) {
        this.encounterDate = date;
    }

    public String getInitiatorId() {
        return initiatorId;
    }

    public void setInitiatorId(String initiatorId) {
        this.initiatorId = initiatorId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

}
