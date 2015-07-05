package edu.usm.service;

import edu.usm.domain.Committee;
import edu.usm.domain.Contact;
import edu.usm.domain.Event;
import edu.usm.domain.Organization;

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
}
