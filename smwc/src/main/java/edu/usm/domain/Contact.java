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
    private String address;
    @Column
    private String phoneNumber;
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
    @ManyToMany
    @JoinTable(
            name="contact_organization",
            joinColumns={@JoinColumn(name="contact_id", referencedColumnName = "id")},
            inverseJoinColumns={@JoinColumn(name="org_id", referencedColumnName = "id")}
    )
    private List<Organization> organizations;

    @OneToOne
    @JoinColumn(name = "donorinfo_id")
    private DonorInfo donorInfo;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "memberinfo_id")
    private MemberInfo memberInfo;

    @Column
    private String interests;

    @ManyToMany
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
}

