package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.io.Serializable;
import java.util.Set;


@Entity(name = "organization")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="organization_id")
public class Organization extends BasicEntity  implements Serializable {


    @Column
    private String name;

    @ManyToMany(mappedBy = "organizations", cascade = {CascadeType.REFRESH})
    private Set<Contact> members;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Contact> getMembers() {
        return members;
    }

    public void setMembers(Set<Contact> members) {
        this.members = members;
    }
}
