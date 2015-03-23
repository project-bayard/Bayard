package edu.usm.domain;


import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity

public class Contact implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @JsonView(Views.ContactList.class)
    private long id;
    @Column
    @JsonView(Views.ContactList.class)
    private String firstName;
    @Column
    @JsonView(Views.ContactList.class)
    private String middleName;
    @Column
    @JsonView(Views.ContactList.class)
    private String lastName;
    @Column
    @JsonView(Views.ContactList.class)
    private String streetAddress;
    @Column
    @JsonView(Views.ContactList.class)
    private String aptNumber;
    @Column
    @JsonView(Views.ContactList.class)
    private String city;
    @Column
    @JsonView(Views.ContactList.class)
    private String zipCode;
    @Column
    @JsonView(Views.ContactList.class)
    private String phoneNumber1;
    @Column
    @JsonView(Views.ContactList.class)
    private String phoneNumber2;
    @Column
    @JsonView(Views.ContactList.class)
    private String email;
    @Column
    @JsonView(Views.ContactList.class)
    private String language;

    @ManyToMany
    @JoinTable(
            name="contact_committee",
            joinColumns={@JoinColumn(name="contact_id", referencedColumnName = "id")},
            inverseJoinColumns={@JoinColumn(name="committee_id", referencedColumnName = "id")}
    )
    private List<Committee> committees;

    @Column
    private String occupation;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name="contact_organization",
            joinColumns={@JoinColumn(name="contact_id", referencedColumnName = "id")},
            inverseJoinColumns={@JoinColumn(name="org_id", referencedColumnName = "id")}
    )
    private List<Organization> organizations;

    @OneToOne(cascade = {CascadeType.ALL} , fetch = FetchType.EAGER)
    @JoinColumn(name = "donorinfo_id")
    private DonorInfo donorInfo;

    @OneToOne(fetch=FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "memberinfo_id")
    private MemberInfo memberInfo;

    @Column
    private String interests;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name="contact_events",
            joinColumns={@JoinColumn(name="contact_id", referencedColumnName = "id")},
            inverseJoinColumns={@JoinColumn(name="event_id", referencedColumnName = "id")}
    )
    private List<Event> attendedEvents;

    @OneToMany(mappedBy = "contact")
    private List<Encounter> encounters;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<Committee> getCommittees() {
        return committees;
    }

    public void setCommittees(List<Committee> committees) {
        this.committees = committees;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    public DonorInfo getDonorInfo() {
        return donorInfo;
    }

    public void setDonorInfo(DonorInfo donorInfo) {
        this.donorInfo = donorInfo;
    }

    public MemberInfo getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(MemberInfo memberInfo) {
        this.memberInfo = memberInfo;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public List<Event> getAttendedEvents() {
        return attendedEvents;
    }

    public void setAttendedEvents(List<Event> attendedEvents) {
        this.attendedEvents = attendedEvents;
    }

    public List<Encounter> getEncounters() {
        return encounters;
    }

    public void setEncounters(List<Encounter> encounters) {
        this.encounters = encounters;
    }

    public String getAptNumber() {
        return aptNumber;
    }

    public void setAptNumber(String aptNumber) {
        this.aptNumber = aptNumber;
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

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }



    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Contact) {
            Contact other = (Contact) obj;
            return this.getId() == other.getId();
        }
        return false;
    }


}

