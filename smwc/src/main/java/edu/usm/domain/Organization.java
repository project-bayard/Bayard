package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;


@Entity(name = "organization")
public class Organization extends BasicEntity  implements Serializable {


    @JsonView({Views.ContactList.class, Views.OrganizationList.class, Views.ContactOrganizationDetails.class})
    @Column
    @NotNull
    @Size(min = 1)
    private String name;

    @JsonView({Views.ContactList.class, Views.OrganizationList.class})
    @ManyToMany(mappedBy = "organizations", cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<Contact> members;

    @Column
    @JsonView(Views.OrganizationList.class)
    private String streetAddress;

    @Column
    @JsonView(Views.OrganizationList.class)
    private String city;

    @Column
    @JsonView(Views.OrganizationList.class)
    private String state;

    @Column
    @JsonView(Views.OrganizationList.class)
    private String zipCode;

    @Column
    @JsonView(Views.OrganizationList.class)
    private String phoneNumber;

    @Column
    @JsonView(Views.OrganizationList.class)
    private String email;

    @Column
    @JsonView(Views.OrganizationList.class)
    private String primaryContactName;

    @Column
    @JsonView(Views.OrganizationList.class)
    private String description;

    public Organization(String id) {
        setId(id);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPrimaryContactName() {
        return primaryContactName;
    }

    public void setPrimaryContactName(String primaryContactName) {
        this.primaryContactName = primaryContactName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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


}
