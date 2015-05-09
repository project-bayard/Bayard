package edu.usm.domain.dto;

import edu.usm.domain.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContactDto implements Serializable {

    private String id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String streetAddress;
    private String aptNumber;
    private String city;
    private String zipCode;
    private String phoneNumber1;
    private String phoneNumber2;
    private String email;
    private String language;
    private String occupation;
    private String interests;
    private boolean donor;
    private boolean member;
    private int assessment;
    private Set<CommitteeDto> committees;
    private Set<OrganizationDto> organizations;
    private List<EventDto> attendedEvents;
    private List<EncounterDto> encounters;
    private DonorInfoDto donorInfo;
    private MemberInfoDto memberInfo;


    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
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

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public boolean isDonor() {
        return donor;
    }

    public void setDonor(boolean donor) {
        this.donor = donor;
    }

    public boolean isMember() {
        return member;
    }

    public void setMember(boolean member) {
        this.member = member;
    }

    public int getAssessment() {
        return assessment;
    }

    public void setAssessment(int assessment) {
        this.assessment = assessment;
    }

    public Set<CommitteeDto> getCommittees() {
        return committees;
    }

    public void setCommittees(Set<CommitteeDto> committees) {
        this.committees = committees;
    }

    public Set<OrganizationDto> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(Set<OrganizationDto> organizations) {
        this.organizations = organizations;
    }

    public List<EventDto> getAttendedEvents() {
        return attendedEvents;
    }

    public void setAttendedEvents(List<EventDto> attendedEvents) {
        this.attendedEvents = attendedEvents;
    }

    public List<EncounterDto> getEncounters() {
        return encounters;
    }

    public void setEncounters(List<EncounterDto> encounters) {
        this.encounters = encounters;
    }

    public DonorInfoDto getDonorInfo() {
        return donorInfo;
    }

    public void setDonorInfo(DonorInfoDto donorInfo) {
        this.donorInfo = donorInfo;
    }

    public MemberInfoDto getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(MemberInfoDto memberInfo) {
        this.memberInfo = memberInfo;
    }

    public Contact convertToContact () {
        Contact contact = new Contact();
        contact.setId(this.getId());

        contact.setFirstName(this.getFirstName());
        contact.setLastName(this.getLastName());
        contact.setMiddleName(this.getMiddleName());
        contact.setStreetAddress(this.getStreetAddress());
        contact.setAptNumber(this.getAptNumber());
        contact.setCity(this.getCity());
        contact.setZipCode(this.getZipCode());
        contact.setLanguage(this.getLanguage());
        contact.setOccupation(this.getOccupation());

        contact.setAssessment(this.getAssessment());
        contact.setEmail(this.getEmail());
        contact.setInterests(this.getInterests());
        contact.setPhoneNumber1(this.getPhoneNumber1());
        contact.setPhoneNumber2(this.getPhoneNumber2());


        contact.setCommittees(convertCommittees());

        contact.setAttendedEvents(convertEvents());
        contact.setOrganizations(convertOrganizations());
        contact.setEncounters(convertEncounters());

        if (this.getDonorInfo() != null)
            contact.setDonorInfo(this.getDonorInfo().convertToDonorInfo());
        if (this.getMemberInfo() != null)
            contact.setMemberInfo(this.getMemberInfo().convertToMemberInfo());

        return contact;
    }

    private Set<Committee> convertCommittees () {

        if (this.getCommittees() != null) {
            Set<Committee> committeeSet = new HashSet<>();

            for (CommitteeDto committeeDto : this.getCommittees()) {
                committeeSet.add(committeeDto.convertToCommittee());
            }

            return committeeSet;
        }

        return null;

    }

    private List<Event> convertEvents() {
        if (this.getAttendedEvents() != null) {
            List<Event> eventList = new ArrayList<>();
            for (EventDto eventDto : this.getAttendedEvents()) {
                eventList.add(eventDto.convertToEvent());
            }

            return eventList;
        }

        return null;

    }

    private Set<Organization> convertOrganizations() {
        if (this.getOrganizations() != null) {
            Set<Organization> organizationSet = new HashSet<>();
            for (OrganizationDto organizationDto : this.getOrganizations()) {
                organizationSet.add(organizationDto.convertToOrganization());
            }

            return organizationSet;
        }

        return null;

    }

    private List<Encounter> convertEncounters () {

        if (this.getEncounters() != null) {
            List<Encounter> encounterList = new ArrayList<>();
            for (EncounterDto encounterDto : this.getEncounters()) {
                encounterList.add(encounterDto.convertToEncounter());
            }
            return encounterList;
        }

        return null;

    }

}
