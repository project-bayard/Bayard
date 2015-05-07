package edu.usm.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Entity(name = "event")
public class Event extends BasicEntity  implements Serializable {

    @Column
    private String name;


    @Column
    private String notes;

    @Column
    private String location;


    @ManyToMany(mappedBy = "attendedEvents", cascade = {CascadeType.ALL})
    private Set<Contact> attendees;

    @Column
    private LocalDate date;

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
