package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * A committee of the organization running Bayard.
 */
@Entity
public class Committee extends Aggregation implements Serializable {

    @ManyToMany(mappedBy = "committees" , cascade = {CascadeType.REFRESH,CascadeType.MERGE} , fetch = FetchType.EAGER)
    @JsonView({Views.GroupDetails.class,
            Views.CommitteeList.class,
            Views.CommitteeDetails.class})
    private Set<Contact> members;

    @OneToMany(mappedBy="committee", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JsonView({Views.CommitteeDetails.class})
    private Set<Event> events;

    @Column
    @NotNull
    @JsonView({Views.CommitteeList.class,
            Views.ContactDetails.class,
            Views.ContactCommitteeDetails.class,
            Views.EventList.class,
            Views.GroupList.class,
            Views.GroupDetails.class,
            Views.CommitteeDetails.class})
    private String name;

    @Override
    public String getAggregationType() {
        return Aggregation.TYPE_COMMITTEE;
    }

    /**
     * @return the members of this Committee
     */
    @Override
    public Set<Contact> getAggregationMembers() {
        return members;
    }

    @Override
    public void setAggregationType(String aggregationType) {
        this.aggregationType = aggregationType;
    }

    @Override
    public void setAggregationMembers(Set<Contact> aggregationMembers) {
        this.members = aggregationMembers;
    }

    public Committee() {
        super();
    }

    /**
     * @param committeeName the name of this Committee
     */
    public Committee(String committeeName) {
        super();
        this.name = committeeName;
    }

    /**
     * @return the Events held by this Committee
     */
    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    /**
     * @return the members of this Committee
     */
    public Set<Contact> getMembers() {
        return members;
    }

    public void setMembers(Set<Contact> members) {
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
