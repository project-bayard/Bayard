package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity(name = "event")
public class Event extends BasicEntity  implements Serializable {

    @Column
    @JsonView({Views.EventList.class})
    private String name;

    @Column
    @JsonView({Views.EventList.class})
    private String notes;

    @Column
    @JsonView({Views.EventList.class})
    private String location;

    @ManyToOne(fetch = FetchType.EAGER , cascade = CascadeType.REFRESH)
    @JoinColumn(name = "committee_id")
    @JsonView({Views.EventList.class})
    private Committee committee;

    /*
    Adding an Event to a Contact's attendedEvents will refresh this set.
    Adding a Contact to an Event's attendees will NOT refresh the Contact's attendedEvents

     */
    @ManyToMany(mappedBy = "attendedEvents", cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JsonView({Views.EventList.class})
    private Set<Contact> attendees;

    @Column
    @JsonView({Views.EventList.class})
    private String dateHeld;

    public Event(String id) {
        setId(id);
    }

    public Event() {
        super();
    }

    public Committee getCommittee() {
        return committee;
    }

    public void setCommittee(Committee committee) {
        this.committee = committee;
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
