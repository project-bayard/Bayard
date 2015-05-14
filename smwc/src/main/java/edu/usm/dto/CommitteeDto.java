package edu.usm.dto;

import edu.usm.domain.Committee;

import java.io.Serializable;
import java.util.Set;

public class CommitteeDto extends BasicEntityDto implements Serializable {

    private String id;
    private String name;
    private Set<String> members;

    public Set<String> getMembers() {
        return members;
    }

    public void setMembers(Set<String> members) {
        this.members = members;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public Committee convertToCommittee () {
        Committee committee = new Committee(this.getId());
        committee.setName(this.getName());
        committee.setLastModified(getLastModified());
        committee.setCreated(getCreated());
        return committee;
    }
}
