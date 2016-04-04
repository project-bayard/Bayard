package edu.usm.service;

import edu.usm.domain.*;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.NotFoundException;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.dto.EncounterDto;
import edu.usm.dto.SignInDto;
import edu.usm.dto.SustainerPeriodDto;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Set;
import java.util.SortedSet;

/**
 * Created by scottkimball on 3/12/15.
 */

public interface ContactService {

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void attendEvent(String contactId, String eventId) throws NullDomainReference.NullContact, NullDomainReference.NullEvent;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void unattendEvent (String contactId, String eventId) throws ConstraintViolation, NullDomainReference.NullContact, NullDomainReference.NullEvent;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Contact findById (String id) throws NullDomainReference.NullContact;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Set<Contact> findAll();

    @PreAuthorize(value = "hasAnyRole('ROLE_ELEVATED','ROLE_SUPERUSER')")
    void delete (String id) throws ConstraintViolation, NullDomainReference;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    String create(Contact contact) throws ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Set<Contact> findByFirstName(String firstName);

    @PreAuthorize(value = "hasAnyRole('ROLE_SUPERUSER')")
    void deleteAll();

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Set<Contact> findAllInitiators();

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void addContactToOrganization(String contactId, String organizationId) throws NullDomainReference.NullOrganization, NullDomainReference.NullContact;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void removeContactFromOrganization(String contactId, String organizationId) throws NullDomainReference.NullContact, NullDomainReference.NullOrganization;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Set<Organization> getAllContactOrganizations(String contactId) throws NullDomainReference.NullContact;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void addContactToCommittee(String contactId, String committeeId) throws  NullDomainReference.NullContact, NullDomainReference.NullCommittee;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void removeContactFromCommittee(String contactId, String committeeId) throws NullDomainReference.NullContact, NullDomainReference.NullCommittee;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Set<Committee> getAllContactCommittees(String contactId) throws NullDomainReference.NullContact;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void updateBasicDetails(String contactId, Contact details) throws  ConstraintViolation, NullDomainReference;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void updateDemographicDetails(String contactId, Contact details) throws  NullDomainReference.NullContact;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void addEncounter (String contactId, String initiatorId,EncounterType encounterType, EncounterDto dto)
            throws ConstraintViolation, NullDomainReference.NullContact, NullDomainReference.NullEncounterType;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void updateMemberInfo(String contactId, MemberInfo memberInfo) throws  NullDomainReference.NullContact;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void removeEncounter(String contactId, String encounterId)  throws NullDomainReference.NullContact, NullDomainReference.NullEncounter;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void removeInitiator(String initiatorId, String encounterId)  throws NullDomainReference.NullContact, NullDomainReference.NullEncounter;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void updateNeedsFollowUp(String contactId, boolean followUp) throws NullDomainReference.NullContact;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    int getUpdatedAssessment(String contactId) throws NullDomainReference.NullContact;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void updateAssessment(String contactId, int assessment) throws NullDomainReference.NullContact ;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void removeFromGroup(String contactId, String groupId) throws NullDomainReference.NullContact, NullDomainReference.NullGroup;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void addToGroup(String contactId, String groupId) throws NullDomainReference.NullContact, NullDomainReference.NullGroup;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Set<Group> getAllContactGroups(String contactId) throws NullDomainReference.NullContact;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Contact findByFirstEmailPhone(SignInDto signInDto) throws NotFoundException;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void addDonation(String contactId, Donation donation) throws  NullDomainReference.NullContact;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void removeDonation(String contactId, String donationId) throws  NullDomainReference;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Set<Donation> getAllContactDonations(String contactId) throws NullDomainReference;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    DonorInfo getDonorInfo(String contactId) throws NullDomainReference;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    SustainerPeriod findSustainerPeriodById(String id);

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void createSustainerPeriod(String contactId, SustainerPeriodDto dto) throws  ConstraintViolation, NullDomainReference;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void updateSustainerPeriod(String contactId, String sustainerPeriodId, SustainerPeriodDto newDetails) throws ConstraintViolation, NullDomainReference;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void deleteSustainerPeriod(String contactId, String sustainerPeriodId) throws ConstraintViolation, NullDomainReference;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    SortedSet<SustainerPeriod> getAllContactSustainerPeriods(String contactId) throws NullDomainReference;

}
