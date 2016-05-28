package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an event held by the organization running Bayard.
 */
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

    @Lob
    @Type(type="org.hibernate.type.StringClobType")
    @Column
    @JsonView({Views.EventList.class})
    private String notes;

    @Column
    @JsonView({Views.EventList.class})
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "committee_id")
    @JsonView({Views.EventList.class})
    private Committee committee;

    @ManyToMany(mappedBy = "attendedEvents", fetch = FetchType.LAZY)
    @JsonView({Views.EventList.class,
            Views.GroupDetails.class})
    private Set<Contact> attendees;

    @Column
    @NotNull
    @JsonView({Views.EventList.class,
            Views.CommitteeDetails.class})
    private String dateHeld;

    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Set<Donation> donations;

    public Event() {
        super();
    }

    /**
     * @param eventName the name of the Event
     * @param dateHeld the date the Event was held
     */
    public Event(String eventName, String dateHeld) {
        super();
        this.name = eventName;
        this.dateHeld = dateHeld;
    }

    @Override
    public String getAggregationType() {
        return Aggregation.TYPE_EVENT;
    }

    /**
     * @return the attendees of the Event
     */
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

    /**
     * @return the Committee associated with this event, if any
     */
    public Committee getCommittee() {
        return committee;
    }

    public void setCommittee(Committee committee) {
        this.committee = committee;
    }

    /**
     * @return the notes pertaining to this Event
     */
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * @return the location where this Event took place
     */
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the Contacts who attended this Event
     */
    public Set<Contact> getAttendees() {
        return attendees;
    }

    public void setAttendees(Set<Contact> attendees) {
        this.attendees = attendees;
    }

    /**
     * @return the Event's name
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the date this Event was held
     */
    public String getDateHeld() {
        return dateHeld;
    }

    public void setDateHeld(String dateHeld) {
        this.dateHeld = dateHeld;
    }

    /**
     * @return the set of Donations, if any, associated with this Event
     */
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
