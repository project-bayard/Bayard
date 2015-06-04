package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity(name = "event")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="event_id")
public class Event extends BasicEntity  implements Serializable {

    @Column
    private String name;

    @Column
    private String notes;

    @Column
    private String location;

    @ManyToMany(mappedBy = "attendedEvents", cascade = {CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<Contact> attendees;

    @Column
    private String dateHeld;

    public Event(String id) {
        setId(id);
    }

    public Event() {
        super();
    }

    //TODO: Need to ask about more attributes of Events

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

    public Set<Contact> getAttendees() {
        return attendees;
    }

    public void setAttendees(Set<Contact> attendees) {
        this.attendees = attendees;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateHeld() {
        return dateHeld;
    }

    public void setDateHeld(String dateHeld) {
        this.dateHeld = dateHeld;
    }
}
