package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * A collection of Contacts known to the organization running Bayard. The fundamental Aggregations are Committees,
 * Organizations and Events.
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Aggregation extends BasicEntity{

    static final String TYPE_ORGANIZATION = "Organization";
    static final String TYPE_COMMITTEE = "Committee";
    static final String TYPE_EVENT = "Event";

    @Column
    @JsonView({Views.GroupList.class,
            Views.GroupDetails.class})
    protected String aggregationType;

    @ManyToMany(mappedBy = "aggregations" , fetch = FetchType.LAZY)
    protected Set<Group> groups = new HashSet<>();

    public Set<Group> getGroups() {
        return groups;
    }
    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    /**
     * @return the type of this Aggregation
     */
    public abstract String getAggregationType();

    /**
     * @return the Contacts that comprise this Aggregation
     */
    public abstract Set<Contact> getAggregationMembers();

    public abstract void setAggregationType(String aggregationType);
    public abstract void setAggregationMembers(Set<Contact> aggregationMembers);

}