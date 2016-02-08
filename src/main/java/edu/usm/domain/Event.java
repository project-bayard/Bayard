package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "event")
public class Event extends Aggregation implements Serializable {

    @Column
    @NotNull
    @Size(min = 1)
    @JsonView({Views.EventList.class,
            Views.GroupList.class,
            Views.GroupDetails.class,
            Views.CommitteeDetails.class})
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
    @JsonView({Views.EventList.class,
            Views.GroupDetails.class})
    private Set<Contact> attendees;

    @Column
    @NotNull
    @JsonView({Views.EventList.class,
            Views.CommitteeDetails.class})
    private String dateHeld;

    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    private Set<Donation> donations;

    public Event(String id) {
        setId(id);
    }

    public Event() {
        super();
    }

    @Override
    public String getAggregationType() {
        return Aggregation.TYPE_EVENT;
    }

    @Override
    public Set<Contact> getAggregationMembers() {
        return attendees;
    }

    @Override
    public void setAggregationType(String aggregationType) {
        this.aggregationType = aggregationType;
    }

    @Override
    public void setAggregationMembers(Set<Contact> aggregationMembers) {
        attendees = aggregationMembers;
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

    public Set<Donation> getDonations() {
        return donations;
    }

    public void setDonations(Set<Donation> donations) {
        this.donations = donations;
    }

    public void addDonation(Donation donation) {
        if (null == this.donations) {
            this.donations = new HashSet<>();
        }
        this.donations.add(donation);
    }
}
