package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity(name = "organization")
public class Organization extends Aggregation implements Serializable {


    @JsonView({Views.ContactList.class, Views.OrganizationList.class, Views.ContactOrganizationDetails.class, Views.GroupList.class,
            Views.GroupDetails.class,})
    @Column
    @NotNull
    @Size(min = 1)
    private String name;

    @JsonView({Views.ContactList.class, Views.OrganizationList.class, Views.GroupDetails.class})
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

    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "organization_id")
    private Set<Donation> donations;

    public Organization(String id) {
        setId(id);
    }

    @Override
    public String getAggregationType() {
        return Aggregation.TYPE_ORGANIZATION;
    }

    @Override
    public Set<Contact> getAggregationMembers() {
        return members;
    }

    @Override
    public void setAggregationType(String aggregationType) {
        this.aggregationType = aggregationType;
    }

    @Override
    public void setAggregationMembers(Set<Contact> aggregationMembers) {
        members = aggregationMembers;
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

    public Set<Donation> getDonations() {
        return donations;
    }

    public void setDonations(Set<Donation> donations) {
        this.donations = donations;
    }

    public void addDonation(Donation donation) {
        if (null == this.donations) {
            this.donations = new HashSet<>();
        }
        this.donations.add(donation);
    }
}
