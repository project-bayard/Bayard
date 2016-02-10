package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a collection of distinct Aggregations and additional, top-level Contacts that form a meaningful
 * unit to the organization running Bayard.
 */
@Entity
@Table(name="alinskygroup")
public class Group extends BasicEntity implements Serializable {

    @Column
    @JsonView({Views.GroupList.class,
            Views.GroupDetails.class,
            Views.GroupPanel.class})
    private String groupName;

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinTable(
            name="alinskygroup_aggregation",
            joinColumns={@JoinColumn(name="alinskygroup_id", referencedColumnName = "id")},
            inverseJoinColumns={@JoinColumn(name="aggregation_id", referencedColumnName = "id")}
    )
    @JsonView({Views.GroupList.class,
            Views.GroupDetails.class})
    private Set<Aggregation> aggregations = new HashSet<>();

    @ManyToMany(mappedBy = "groups", cascade = {CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JsonView({Views.GroupList.class,
            Views.GroupDetails.class})
    private Set<Contact> topLevelMembers = new HashSet<>();

    public Group() {
        super();
    }

    /**
     * @param groupName the name of the Group
     */
    public Group(String groupName) {
        super();
        this.groupName = groupName;
    }

    /**
     * @return the name of the Group
     */
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * @return the set of Aggregations that comprise the Group
     */
    public Set<Aggregation> getAggregations() {
        return aggregations;
    }

    public void setAggregations(Set<Aggregation> aggregations) {
        this.aggregations = aggregations;
    }

    public Set<Contact> getTopLevelMembers() {
        return topLevelMembers;
    }

    public void setTopLevelMembers(Set<Contact> topLevelMembers) {
        this.topLevelMembers = topLevelMembers;
    }
}
