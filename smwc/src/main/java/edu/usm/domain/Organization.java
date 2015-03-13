package edu.usm.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by justin on 2/19/15.
 */
@Entity
public class Organization  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String name;

    @ManyToMany(mappedBy = "organizations", cascade = {CascadeType.ALL})
    private List<Contact> members;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
