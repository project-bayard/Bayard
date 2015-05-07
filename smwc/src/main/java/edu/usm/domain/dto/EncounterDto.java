package edu.usm.domain.dto;

import edu.usm.domain.Encounter;

import java.io.Serializable;

public class EncounterDto implements Serializable {

    private String id;
    private String subjectId;
    private String initiatorId;
    private String notes;
    private int assessment;
    private String type;
    private String formId;

    public static EncounterDto fromEncounter(Encounter encounter) {
        EncounterDto dto = new EncounterDto();
        dto.setId(encounter.getId());
        dto.setSubjectId(encounter.getContact().getId());
        dto.setInitiatorId(encounter.getInitiator().getId());
        dto.setNotes(encounter.getNotes());
        dto.setAssessment(encounter.getAssessment());
        dto.setType(encounter.getType().toString());
        dto.setFormId(encounter.getForm().getId());
        return dto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
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

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }
}
