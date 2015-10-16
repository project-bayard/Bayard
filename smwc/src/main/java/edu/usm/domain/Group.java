package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by andrew on 10/7/15.
 */

@Entity
@Table(name="alinskygroup")
public class Group extends BasicEntity implements Serializable {

    @Column
    @JsonView({Views.GroupListView.class,
            Views.GroupDetailsView.class,
            Views.GroupPanelView.class})
    private String groupName;

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinTable(
            name="alinskygroup_aggregation",
            joinColumns={@JoinColumn(name="alinskygroup_id", referencedColumnName = "id")},
            inverseJoinColumns={@JoinColumn(name="aggregation_id", referencedColumnName = "id")}
    )
    @JsonView({Views.GroupListView.class,
            Views.GroupDetailsView.class})
    private Set<Aggregation> aggregations = new HashSet<>();

    @ManyToMany(mappedBy = "groups", cascade = {CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JsonView({Views.GroupListView.class,
            Views.GroupDetailsView.class})
    private Set<Contact> topLevelMembers = new HashSet<>();

    public Group() {
        super();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

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
