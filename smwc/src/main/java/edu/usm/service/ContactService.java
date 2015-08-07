package edu.usm.service;

import edu.usm.domain.*;
import edu.usm.dto.EncounterDto;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Set;

/**
 * Created by scottkimball on 3/12/15.
 */

public interface ContactService {

    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    void attendEvent(Contact contact, Event event);

    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    void unattendEvent (Contact contact, Event event);

    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    Contact findById (String id);

    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    Set<Contact> findAll();

    @PreAuthorize(value = "hasAuthority('ROLE_ELEVATED')")
    void delete (Contact contact);

    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    String create(Contact contact);

    @PreAuthorize(value = "hasAuthority('ROLE_SUPERUSER')")
    void deleteAll();

    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    Set<Contact> findAllInitiators();

    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    void addContactToOrganization(Contact contact, Organization organization);

    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    void removeContactFromOrganization(Contact contact, Organization organization);

    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    void addContactToCommittee(Contact contact, Committee committee);

    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    void removeContactFromCommittee(Contact contact, Committee committee);

    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    void updateBasicDetails(Contact contact, Contact details);

    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    void updateDemographicDetails(Contact contact, Contact details);

    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    void addEncounter (Contact contact, Contact initiator, EncounterDto dto);

    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    void updateMemberInfo(Contact contact, MemberInfo memberInfo);

    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    void removeEncounter(Contact contact, Encounter encounter);

    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    void removeInitiator(Contact initiator, Encounter encounter);

}
