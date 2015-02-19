package edu.usm.domain;

import javax.persistence.*;
import java.util.List;

/**
 * Created by justin on 2/19/15.
 */
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String notes;

    @Column
    private String location;

    @ManyToMany(mappedBy = "attendedEvents")
    private List<Contact> attendees;

    @Column
    private LocalDateTime date;

    //TODO: Need to ask about more attributes of Events

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Contact> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<Contact> attendees) {
        this.attendees = attendees;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
