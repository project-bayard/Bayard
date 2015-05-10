package edu.usm.domain.dto;

import edu.usm.domain.Organization;

import java.io.Serializable;
import java.util.Set;

public class OrganizationDto implements Serializable {

    private String id;
    private String name;
    private Set<String> members;

    public Organization convertToOrganization () {
        Organization organization;
        if (this.getId() != null)
            organization = new Organization(this.getId());
        else
            organization = new Organization();

        organization.setName(this.getName());
        return organization;
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

    public Set<String> getMembers() {
        return members;
    }

    public void setMembers(Set<String> members) {
        this.members = members;
    }
}
