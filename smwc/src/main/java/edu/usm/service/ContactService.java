package edu.usm.service;

import edu.usm.domain.*;
import edu.usm.dto.EncounterDto;

import java.util.Set;

/**
 * Created by scottkimball on 3/12/15.
 */

public interface ContactService {

    void attendEvent(Contact contact, Event event);
    void unattendEvent (Contact contact, Event event);
    Contact findById (String id);
    Set<Contact> findAll();
    void delete (Contact contact);
    String create(Contact contact);
    void deleteAll();
    Set<Contact> findAllInitiators();
    void addContactToOrganization(Contact contact, Organization organization);
    void removeContactFromOrganization(Contact contact, Organization organization);
    void addContactToCommittee(Contact contact, Committee committee);
    void removeContactFromCommittee(Contact contact, Committee committee);
    void updateBasicDetails(Contact contact, Contact details);
    void updateDemographicDetails(Contact contact, Contact details);
    void addEncounter (Contact contact, Contact initiator, EncounterDto dto);
    void updateMemberInfo(Contact contact, MemberInfo memberInfo);

}
