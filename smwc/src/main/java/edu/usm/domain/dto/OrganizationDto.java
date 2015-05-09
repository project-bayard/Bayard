package edu.usm.domain.dto;

import edu.usm.domain.Organization;

import java.io.Serializable;

public class OrganizationDto implements Serializable {

    private String id;
    private String name;

    public Organization convertToOrganization () {
        Organization organization = new Organization(this.getId());
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
}
