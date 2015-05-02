package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="committee_id")
public class Committee extends BasicEntity implements Serializable {


    @ManyToMany(mappedBy = "committees" , cascade = CascadeType.REFRESH)
    @JsonIgnore
    private Set<Contact> members;

    @Column
    @JsonView({Views.ContactDetails.class})
    private String name;

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
