package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity(name = "event")
@SQLDelete(sql="UPDATE event SET deleted = '1' WHERE id = ?")
@Where(clause="deleted <> '1'")
public class Event extends BasicEntity  implements Serializable {

    @Column
    @JsonView({Views.ContactDetails.class})
    private String name;


    @Column
    private String notes;

    @Column
    private String location;


    @ManyToMany(mappedBy = "attendedEvents", cascade = {CascadeType.ALL})
    private List<Contact> attendees;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
