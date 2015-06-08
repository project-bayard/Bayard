package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="committee_id")
public class Committee extends BasicEntity implements Serializable {

    @ManyToMany(mappedBy = "committees" , cascade = {CascadeType.REFRESH,CascadeType.MERGE} , fetch = FetchType.EAGER)
    @JsonView(Views.CommitteeList.class)
    private Set<Contact> members;

    @Column
    @JsonView(Views.CommitteeList.class)
    private String name;

    public Committee (String id) {
        setId(id);
    }

    public Committee() {
        super();
    }

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
