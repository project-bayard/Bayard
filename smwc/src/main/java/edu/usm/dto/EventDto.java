package edu.usm.dto;

/**
 * Created by Andrew on 7/8/2015.
 */
public class EventDto {

    private String committeeId;
    private String notes;
    private String name;
    private String location;
    private String dateHeld;

    public String getCommitteeId() {
        return committeeId;
    }

    public void setCommitteeId(String committeeId) {
        this.committeeId = committeeId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateHeld() {
        return dateHeld;
    }

    public void setDateHeld(String dateHeld) {
        this.dateHeld = dateHeld;
    }
}
