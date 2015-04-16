package edu.usm.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.io.Serializable;
import java.util.List;


@Entity(name = "organization")
public class Organization extends BasicEntity  implements Serializable {


    @Column
    private String name;

    @ManyToMany(mappedBy = "organizations", cascade = {CascadeType.REFRESH})
    private List<Contact> members;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Contact> getMembers() {
        return members;
    }

    public void setMembers(List<Contact> members) {
        this.members = members;
    }
}
