package edu.usm.it.service;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Committee;
import edu.usm.domain.Contact;
import edu.usm.domain.Organization;
import edu.usm.dto.EncounterDto;
import edu.usm.service.CommitteeService;
import edu.usm.service.ContactService;
import edu.usm.service.OrganizationService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;


/**
 * Created by scottkimball on 3/12/15.
 */
public class ContactServiceTest extends WebAppConfigurationAware {

    @Autowired
    ContactService contactService;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    CommitteeService committeeService;



    private Contact contact;
    private Contact contact2;
    private Organization organization;
    private Committee committee;

    @Before
    public void setup() {
        contact = new Contact();
        contact.setFirstName("First");
        contact.setLastName("Last");
        contact.setStreetAddress("123 Fake St");
        contact.setAptNumber("# 4");
        contact.setCity("Portland");
        contact.setZipCode("04101");
        contact.setEmail("email@gmail.com");

        contact2 = new Contact();
        contact2.setFirstName("FirstName");
        contact2.setLastName("LastNAme");
        contact2.setStreetAddress("456 Fake St");
        contact2.setAptNumber("# 4");
        contact2.setCity("Lewiston");
        contact2.setZipCode("04108");
        contact2.setEmail("email@gmail.com");

        organization = new Organization();
        organization.setName("organization");
        committee = new Committee();
        committee.setName("committee");

    }

    @After
    public void teardown() {
        contactService.deleteAll();
        organizationService.deleteAll();
        committeeService.deleteAll();
    }


    @Test
    @Transactional
    public void testCreateAndFind () throws Exception {

        contactService.create(contact);
        contactService.create(contact2);

        Contact fromDb = contactService.findById(contact.getId());
        assertNotNull(fromDb);
        assertEquals(fromDb,contact);
        assertEquals(fromDb.getId(), contact.getId());
        assertEquals(fromDb.getLastName(), contact.getLastName());
        assertEquals(fromDb.getFirstName(), contact.getFirstName());
        assertEquals(fromDb.getEmail(), contact.getEmail());
        assertEquals(fromDb.getStreetAddress(), contact.getStreetAddress());
        assertEquals(fromDb.getAptNumber(), contact.getAptNumber());
        assertEquals(fromDb.getCity(), contact.getCity());
        assertEquals(fromDb.getZipCode(), contact.getZipCode());


        Set<Contact> contacts = contactService.findAll();
        assertEquals(contacts.size(),2);
        assertTrue(contacts.contains(contact));
        assertTrue(contacts.contains(contact2));


    }

    @Test
    public void testFindAllAndUpdateList () throws Exception {

        Contact contact = new Contact();
        contact.setFirstName("First");
        contact.setLastName("Last");
        contact.setStreetAddress("123 Fake St");
        contact.setAptNumber("# 4");
        contact.setCity("Portland");
        contact.setZipCode("04101");
        contact.setEmail("email@gmail.com");

        Contact contact2 = new Contact();
        contact2.setFirstName("FirstName");
        contact2.setLastName("LastNAme");
        contact2.setStreetAddress("456 Fake St");
        contact2.setAptNumber("# 4");
        contact2.setCity("Lewiston");
        contact2.setZipCode("04108");
        contact2.setEmail("email@gmail.com");

        List<Contact> toDb = new ArrayList<>();

        contactService.create(contact);
        contactService.create(contact2);
        Set<Contact> contacts = contactService.findAll();

        assertNotNull(contacts);
        assertEquals(contacts.size(),2);
    }

    @Test
    @Transactional
    public void testDelete () {
        contactService.create(contact);
        contactService.create(contact2);

        organizationService.create(organization);

        contactService.addContactToOrganization(contact,organization);
        contactService.addContactToOrganization(contact2,organization);

        contactService.delete(contact);

        Organization fromDb = organizationService.findById(organization.getId());

        assertNull(contactService.findById(contact.getId()));
        assertNotNull(fromDb);
        assertFalse(fromDb.getMembers().contains(contact));
        assertTrue(fromDb.getMembers().contains(contact2));

    }



    @Test
    public void testAddAndRemoveContactFromOrganization () throws Exception {
        contactService.create(contact);
        organizationService.create(organization);
        contactService.addContactToOrganization(contact,organization);

        Contact fromDb = contactService.findById(contact.getId());
        assertNotNull(fromDb);
        assertNotNull(fromDb.getOrganizations());
        assertTrue(fromDb.getOrganizations().contains(organization));

        contactService.removeContactFromOrganization(contact, organization);

        fromDb = contactService.findById(contact.getId());
        assertNotNull(fromDb);
        assertNotNull(fromDb.getOrganizations());
        assertFalse(fromDb.getOrganizations().contains(organization));

        Organization orgFromDb = organizationService.findById(organization.getId());
        assertNotNull(orgFromDb);
        assertNotNull(orgFromDb.getMembers());
        assertFalse(orgFromDb.getMembers().contains(contact));

    }

    @Test
    public void testAddAndRemoveContactFromCommittee () throws Exception {
        contactService.create(contact);
        committeeService.create(committee);

        contactService.addContactToCommittee(contact, committee);

        Contact fromDb = contactService.findById(contact.getId());
        assertNotNull(fromDb);
        assertNotNull(fromDb.getCommittees());
        assertTrue(fromDb.getCommittees().contains(committee));

        contactService.removeContactFromCommittee(contact, committee);

        fromDb = contactService.findById(contact.getId());
        assertNotNull(fromDb);
        assertNotNull(fromDb.getCommittees());
        assertFalse(fromDb.getCommittees().contains(committee));

        Committee committeeFromDb = committeeService.findById(committee.getId());
        assertNotNull(committeeFromDb);
        assertNotNull(committeeFromDb.getMembers());
        assertFalse(committeeFromDb.getMembers().contains(contact));

    }

    @Test
    public void testUpdateBasicDetails () throws Exception {
        contactService.create(contact);

        Contact details = new Contact();
        details.setFirstName("newFirstName");
        details.setLastName("newLastName");
        details.setStreetAddress("123 Fake St");
        details.setAptNumber("# 4");
        details.setCity("Portland");
        details.setZipCode("04101");
        details.setEmail("email@gmail.com");

        contactService.updateBasicDetails(contact, details);

        Contact fromDb = contactService.findById(contact.getId());
        assertEquals(fromDb.getFirstName(), details.getFirstName());
        assertEquals(fromDb.getLastName(), details.getLastName());
        assertEquals(fromDb.getStreetAddress(), details.getStreetAddress());

    }

    @Test
    @Transactional
    public void testAddEncounter () throws Exception {
        String id = contactService.create(contact);
        String initiatorId = contactService.create(contact2);

        EncounterDto dto = new EncounterDto();
        dto.setType("CALL");
        dto.setEncounterDate("2012-01-01");
        dto.setNotes("Notes!");

        contactService.addEncounter(contact,contact2, dto);

        Contact fromDb = contactService.findById(contact.getId());

        assertNotNull(fromDb.getEncounters());
        assertEquals(fromDb.getEncounters().first().getContact().getId(),contact.getId());
        assertEquals(fromDb.getEncounters().first().getInitiator().getId(), contact2.getId());
        assertEquals(fromDb.getEncounters().first().getAssessment(), dto.getAssessment());

        Contact initiatorFromDb = contactService.findById(initiatorId);
        assertNotNull(initiatorFromDb.getEncountersInitiated());
        assertEquals(1, initiatorFromDb.getEncountersInitiated().size());

    }
}