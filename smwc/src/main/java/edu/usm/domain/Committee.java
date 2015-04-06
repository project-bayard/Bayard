package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Committee implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToMany(mappedBy = "committees")
    private List<Contact> members;

    @Column
    @JsonView({Views.ContactDetails.class})
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Contact> getMembers() {
        return members;
    }

    public void setMembers(List<Contact> members) {
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
