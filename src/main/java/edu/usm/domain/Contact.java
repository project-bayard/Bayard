package edu.usm.domain;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@Entity(name = "contact")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Contact extends BasicEntity implements Serializable {


    @Column
    @NotNull
    @Size(min = 1)
    @JsonView({Views.ContactList.class,
            Views.OrganizationList.class,
            Views.CommitteeList.class,
            Views.EventList.class,
            Views.ContactDetails.class,
            Views.ContactEncounterDetails.class,
            Views.GroupListView.class,
            Views.GroupDetailsView.class})
    private String firstName;

    @Column
    @JsonView({Views.ContactList.class,
            Views.OrganizationList.class,
            Views.CommitteeList.class,
            Views.EventList.class,
            Views.ContactDetails.class,
            Views.ContactEncounterDetails.class,
            Views.GroupListView.class,
            Views.GroupDetailsView.class})
    private String middleName;

    @Column
    @JsonView({Views.ContactList.class,
            Views.OrganizationList.class,
            Views.CommitteeList.class,
            Views.EventList.class,
            Views.ContactDetails.class,
            Views.ContactEncounterDetails.class,
            Views.GroupListView.class,
            Views.GroupDetailsView.class})
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
    private String state;

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
            Views.ContactDetails.class,
            Views.GroupDetailsView.class})
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
            Views.ContactDetails.class,
            Views.GroupDetailsView.class})
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
            Views.ContactDetails.class,
            Views.GroupDetailsView.class})
    private int assessment;

    @Column
    @JsonView({Views.ContactList.class,
            Views.ContactDetails.class})
    private boolean initiator;

    @Column
    @JsonView(
            {Views.DemographicDetails.class}
    )
    private String race;

    @Column
    @JsonView(
            {Views.DemographicDetails.class}
    )
    private String ethnicity;

    @Column
    @JsonView(
            {Views.DemographicDetails.class}
    )
    private String dateOfBirth;

    @Column
    @JsonView(
            {Views.DemographicDetails.class}
    )
    private String gender;

    @Column
    @JsonView(
            {Views.DemographicDetails.class}
    )
    private boolean disabled;

    @Column
    @JsonView(
            {Views.DemographicDetails.class}
    )
    private String sexualOrientation;

    @Column
    @JsonView(
            {Views.DemographicDetails.class}
    )
    private String incomeBracket;

    @Column
    @JsonView(
            {Views.ContactList.class, Views.ContactDetails.class}
    )
    private boolean needsFollowUp;


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

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinTable(
            name="contact_alinskygroup",
            joinColumns={@JoinColumn(name="contact_id", referencedColumnName = "id")},
            inverseJoinColumns={@JoinColumn(name="alinskygroup_id", referencedColumnName = "id")}
    )
    private Set<Group> groups;

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

    @OneToMany(mappedBy="contact", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @SortNatural
    private SortedSet<Encounter> encounters = new TreeSet<>();

    @OneToMany(mappedBy="initiator", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @SortNatural
    private SortedSet<Encounter> encountersInitiated;

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public void setInitiator(boolean initiator) {
        this.initiator = initiator;
    }

    public boolean isInitiator() {
        return initiator;
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

    public SortedSet<Encounter> getEncounters() {
        return encounters;
    }

    //Setters for a collection marked for orphanRemoval must not lose reference to the collection first instantiated by Hibernate
    public void setEncounters(SortedSet<Encounter> encounters) {
        this.encounters.clear();
        this.encounters.addAll(encounters);
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public SortedSet<Encounter> getEncountersInitiated() {
        return encountersInitiated;
    }

    public void setEncountersInitiated(SortedSet<Encounter> encountersInitiated) {
        this.encountersInitiated = encountersInitiated;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getSexualOrientation() {
        return sexualOrientation;
    }

    public void setSexualOrientation(String sexualOrientation) {
        this.sexualOrientation = sexualOrientation;
    }

    public String getIncomeBracket() {
        return incomeBracket;
    }

    public void setIncomeBracket(String incomeBracket) {
        this.incomeBracket = incomeBracket;
    }

    public boolean needsFollowUp() {
        return needsFollowUp;
    }

    public void setNeedsFollowUp(boolean needsFollowUp) {
        this.needsFollowUp = needsFollowUp;
    }
}

