package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Entity(name = "organization")
@SQLDelete(sql="UPDATE organization SET deleted = '1' WHERE id = ?")
@Where(clause="deleted <> '1'")
public class Organization extends BasicEntity  implements Serializable {


    @Column
    @JsonView({Views.ContactDetails.class})
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
