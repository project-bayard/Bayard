package edu.usm.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Contact {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @Column
    private String firstName;
    @Column
    private String middleName;
    @Column
    private String lastName;
    @Column
    private String streetAddress;
    @Column
    private String aptNumber;
    @Column
    private String city;
    @Column
    private int zipCode;
    @Column
    private String phoneNumber1;
    @Column
    private String phoneNumber2;
    @Column
    private String email;
    @Column
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

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "donorinfo_id")
    private DonorInfo donorInfo;

    @OneToOne(fetch=FetchType.LAZY, cascade = {CascadeType.ALL})
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

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }
}

