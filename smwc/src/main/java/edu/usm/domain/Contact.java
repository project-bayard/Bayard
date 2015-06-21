package edu.usm.domain;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Entity(name = "contact")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Contact extends BasicEntity implements Serializable {


    @Column
    @JsonView({Views.ContactList.class,
            Views.OrganizationList.class,
            Views.CommitteeList.class,
            Views.EventList.class,
            Views.ContactDetails.class,
            Views.ContactEncounterDetails.class})
    private String firstName;

    @Column
    @JsonView({Views.ContactList.class,
            Views.OrganizationList.class,
            Views.CommitteeList.class,
            Views.EventList.class,
            Views.ContactDetails.class,
            Views.ContactEncounterDetails.class})
    private String middleName;

    @Column
    @JsonView({Views.ContactList.class,
            Views.OrganizationList.class,
            Views.CommitteeList.class,
            Views.EventList.class,
            Views.ContactDetails.class,
            Views.ContactEncounterDetails.class})
    private String lastName;

    @Column
    @JsonView({Views.ContactList.class,
            Views.OrganizationList.class,
            Views.CommitteeList.class,
            Views.EventList.class,
            Views.ContactDetails.class})
    private String streetAddress;

    @Column
    @JsonView({Views.ContactList.class,
            Views.OrganizationList.class,
            Views.CommitteeList.class,
            Views.EventList.class,
            Views.ContactDetails.class})
    private String aptNumber;

    @Column
    @JsonView({Views.ContactList.class,
            Views.OrganizationList.class,
            Views.CommitteeList.class,
            Views.EventList.class,
            Views.ContactDetails.class})
    private String city;

    @Column
    @JsonView({Views.ContactList.class,
            Views.OrganizationList.class,
            Views.CommitteeList.class,
            Views.EventList.class,
            Views.ContactDetails.class})
    private String zipCode;

    @Column
    @JsonView({Views.ContactList.class,
            Views.OrganizationList.class,
            Views.CommitteeList.class,
            Views.EventList.class,
            Views.ContactDetails.class})
    private String phoneNumber1;

    @Column
    @JsonView({Views.ContactList.class,
            Views.OrganizationList.class,
            Views.CommitteeList.class,
            Views.EventList.class,
            Views.ContactDetails.class})
    private String phoneNumber2;

    @Column
    @JsonView({Views.ContactList.class,
            Views.OrganizationList.class,
            Views.CommitteeList.class,
            Views.EventList.class,
            Views.ContactDetails.class})
    private String email;

    @Column
    @JsonView({Views.ContactList.class,
            Views.OrganizationList.class,
            Views.CommitteeList.class,
            Views.EventList.class,
            Views.ContactDetails.class})
    private String language;

    @Column
    @JsonView({Views.ContactList.class,
            Views.OrganizationList.class,
            Views.CommitteeList.class,
            Views.EventList.class,
            Views.ContactDetails.class})
    private String occupation;

    @Column
    @JsonView({Views.ContactList.class,
            Views.OrganizationList.class,
            Views.CommitteeList.class,
            Views.EventList.class,
            Views.ContactDetails.class})
    private String interests;

    @Column
    @JsonView({Views.ContactList.class,
            Views.OrganizationList.class,
            Views.CommitteeList.class,
            Views.EventList.class,
            Views.ContactDetails.class})
    private boolean donor;

    @Column
    @JsonView({Views.ContactList.class,
            Views.OrganizationList.class,
            Views.CommitteeList.class,
            Views.EventList.class,
            Views.ContactDetails.class})
    private boolean member;

    @Column
    @JsonView({Views.ContactList.class,
            Views.OrganizationList.class,
            Views.CommitteeList.class,
            Views.EventList.class,
            Views.ContactDetails.class})
    private int assessment;

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinTable(
            name="contact_committee",
            joinColumns={@JoinColumn(name="contact_id", referencedColumnName = "id")},
            inverseJoinColumns={@JoinColumn(name="committee_id", referencedColumnName = "id")}
    )
    private Set<Committee> committees;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(
            name="contact_organization",
            joinColumns={@JoinColumn(name="contact_id", referencedColumnName = "id")},
            inverseJoinColumns={@JoinColumn(name="org_id", referencedColumnName = "id")}
    )
    private Set<Organization> organizations;

    @OneToOne(cascade = {CascadeType.ALL} , fetch = FetchType.EAGER)
    @JoinColumn(name = "donorinfo_id")
    private DonorInfo donorInfo;

    @OneToOne(fetch=FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "memberinfo_id")
    private MemberInfo memberInfo;

    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name="contact_events",
            joinColumns={@JoinColumn(name="contact_id", referencedColumnName = "id")},
            inverseJoinColumns={@JoinColumn(name="event_id", referencedColumnName = "id")}
    )
    private Set<Event> attendedEvents;

    @OneToMany(mappedBy="contact", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Encounter> encounters;

    @OneToMany(mappedBy="initiator", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private List<Encounter> encountersInitiated;

    @Column
    private boolean initiator;

    public boolean isInitiator() {
        return initiator;
    }

    public Contact (String id) {
        setId(id);
    }

    public Contact() {
        super();
    }

    public void setInitiator(boolean initiator) {
        this.initiator = initiator;
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

    public Set<Committee> getCommittees() {
        return committees;
    }

    public void setCommittees(Set<Committee> committees) {
        this.committees = committees;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public Set<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(Set<Organization> organizations) {
        this.organizations = organizations;
    }

    public DonorInfo getDonorInfo() {
        return donorInfo;
    }

    public void setDonorInfo(DonorInfo donorInfo) {
        donor = donorInfo != null;
        this.donorInfo = donorInfo;
    }

    public MemberInfo getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(MemberInfo memberInfo) {
        member = memberInfo != null;
        this.memberInfo = memberInfo;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public Set<Event> getAttendedEvents() {
        return attendedEvents;
    }

    public void setAttendedEvents(Set<Event> attendedEvents) {
        this.attendedEvents = attendedEvents;
    }

    public List<Encounter> getEncounters() {
        return encounters;
    }

    public void setEncounters(List<Encounter> encounters) {
        if (encounters != null && encounters.size() > 0) {
            Collections.sort(encounters);
            setAssessment(encounters.get(0).getAssessment());

        }
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

    public boolean isDonor() {
        return donor;
    }


    public boolean isMember() {
        return member;
    }


    public int getAssessment() {
        return assessment;
    }

    public void setAssessment(int assessment) {
        this.assessment = assessment;
    }

    public List<Encounter> getEncountersInitiated() {
        return encountersInitiated;
    }

    public void setEncountersInitiated(List<Encounter> encountersInitiated) {
        this.encountersInitiated = encountersInitiated;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Contact) {
            Contact other = (Contact) obj;
            return this.getId().equals(other.getId());
        }
        return false;
    }

}

