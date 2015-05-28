package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;


@Entity(name = "organization")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="organization_id")
public class Organization extends BasicEntity  implements Serializable {


    @JsonView({Views.ContactList.class, Views.OrganizationList.class})
    @Column
    private String name;

    @JsonView({Views.ContactList.class, Views.OrganizationList.class})
    @ManyToMany(mappedBy = "organizations", cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<Contact> members;

    public Organization(String id) {
        setId(id);
    }

    public Organization() {
        super();
    }

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

    @Override
    public boolean equals(Object o) {
       return super.equals(o);
    }


}
