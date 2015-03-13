package edu.usm.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by justin on 2/19/15.
 */
@Entity
public class Event  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String notes;

    @Column
    private String location;

    @ManyToMany(mappedBy = "attendedEvents", cascade = {CascadeType.ALL})
    private List<Contact> attendees;

    @Column
    private LocalDate date;

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
