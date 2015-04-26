package edu.usm.domain.dto;

import java.io.Serializable;
import java.util.List;

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
    private String getPhoneNumber2;
    private String email;
    private String language;
    private String occupation;
    private String interests;
    private boolean donor;
    private boolean member;
    private int assessment;
    private List<CommitteeDto> comittees;
    private List<OrganizationDto> organizations;
    private List<EventDto> attendedEvents;
    private List<EncounterDto> encounters;

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

    public String getGetPhoneNumber2() {
        return getPhoneNumber2;
    }

    public void setGetPhoneNumber2(String getPhoneNumber2) {
        this.getPhoneNumber2 = getPhoneNumber2;
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

    public List<CommitteeDto> getComittees() {
        return comittees;
    }

    public void setComittees(List<CommitteeDto> comittees) {
        this.comittees = comittees;
    }

    public List<OrganizationDto> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<OrganizationDto> organizations) {
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
}
