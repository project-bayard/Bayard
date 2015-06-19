package edu.usm.service.impl;

import edu.usm.domain.*;
import edu.usm.repository.ContactDao;
import edu.usm.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by scottkimball on 3/12/15.
 */
@Service
public class ContactServiceImpl extends BasicService implements ContactService {

    @Autowired
    private ContactDao contactDao;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private CommitteeService committeeService;
    @Autowired
    private EventService eventService;

    private Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);


    @Override
    public Contact findById(String id) {
        logger.debug("Finding contact with ID: " + id);
        return contactDao.findOne(id);
    }

    @Override
    public Set<Contact> findAll() {
        logger.debug("Finding all Contacts");
        return (Set<Contact>) contactDao.findAll();
    }

    @Override
    public void delete(Contact contact) {
        logger.debug("Deleting contact " + contact.getId() );
        logger.debug("Time: " + LocalDateTime.now());

        updateLastModified(contact);

        /*Remove from organizations */
        if (contact.getOrganizations() != null) {
            for(Organization organization : contact.getOrganizations()) {
                organization.getMembers().remove(contact);
                organizationService.update(organization);
            }
        }

        /*Remove from committees*/
        if (contact.getCommittees() != null) {
            for(Committee committee : contact.getCommittees()) {
                committee.getMembers().remove(contact);
                committeeService.update(committee);
            }
        }

         /*Remove from attended events*/
        if (contact.getAttendedEvents() != null) {
            for(Event event : contact.getAttendedEvents()) {
                event.getAttendees().remove(contact);
                eventService.update(event);
            }
        }

        if (contact.getEncountersInitiated() != null) {
            for (Encounter encounter : contact.getEncountersInitiated()) {
                encounter.setInitiator(null);
                this.update(encounter.getContact());
            }
        }

        contactDao.delete(contact);
    }

    @Override
    public void update(Contact contact) {
        logger.debug("Updating contact with ID: " + contact.getId());
        logger.debug("Time: " + LocalDateTime.now());
        updateLastModified(contact);
        contactDao.save(contact);
    }



    @Override
    public String create(Contact contact) {
        logger.debug("Creating contact with ID: " + contact.getId());
        logger.debug("Time: " + LocalDateTime.now());
        contactDao.save(contact);
        return contact.getId();
    }

    @Override
    public void deleteAll() {

        logger.debug("Deleting all contacts.");
        Set<Contact> contacts = findAll();
        contacts.stream().forEach(this::delete);
    }

    @Override
    public Set<Contact> findAllInitiators() {
        logger.debug("Getting all Initiators");
        return contactDao.findAllInitiators();
    }

    @Override
    public void addContactToOrganization(Contact contact, Organization organization) {
        Set<Organization> organizations = contact.getOrganizations();
        Set<Contact> members = organization.getMembers();

        if (organizations == null) {
            organizations = new HashSet<>();
            contact.setOrganizations(organizations);
        }

        if (organization.getMembers() == null) {
            members = new HashSet<>();
            organization.setMembers(members);
        }

        members.add(contact);
        organizations.add(organization);
        update(contact);
    }

}
