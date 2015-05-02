package edu.usm.it.service;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Committee;
import edu.usm.domain.Contact;
import edu.usm.domain.Organization;
import edu.usm.service.CommitteeService;
import edu.usm.service.ContactService;
import edu.usm.service.OrganizationService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
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

        /*Test deleted from Organization member list*/
        Set<Contact> contacts = new HashSet<>();
        contacts.add(contact);
        contacts.add(contact2);

        organization.setMembers(contacts);
        organizationService.create(organization);

        committee.setMembers(contacts);
        committeeService.create(committee);

        Set<Organization> organizations = new HashSet<>();
        organizations.add(organization);
        contact.setOrganizations(organizations);
        contactService.create(contact);
        contactService.create(contact2);

        contactService.delete(contact);

        Organization fromDb = organizationService.findById(organization.getId());


        assertNull(contactService.findById(contact.getId()));
        assertNotNull(fromDb);
        assertFalse(fromDb.getMembers().contains(contact));
        assertTrue(fromDb.getMembers().contains(contact2));

        Committee fromDbCommittee = committeeService.findById(committee.getId());
        assertNotNull(fromDbCommittee);
        assertFalse(fromDbCommittee.getMembers().contains(contact));
        assertTrue(fromDbCommittee.getMembers().contains(contact2));

    }

    @Test
    @Transactional
    public void testUpdateCommittees () {

        Set<Contact> contacts = new HashSet<>();
        contacts.add(contact);


        Set<Committee> committees = new HashSet<>();
        committees.add(committee);
        contact.setCommittees(committees);
        committee.setMembers(contacts);
        committeeService.create(committee);

        contactService.create(contact);

        Contact contactFromDb = contactService.findById(contact.getId());

        /*Before*/
        assertEquals(contactFromDb.getCommittees().size(),committees.size());

        /*Test add new committee*/
        Committee newCommittee = new Committee();
        newCommittee.setName("new_committee");
        newCommittee.setMembers(contacts);
        committeeService.create(newCommittee);

        committees.add(newCommittee);

        contactFromDb.setCommittees(committees);

        contactService.update(contactFromDb);

        contactFromDb = contactService.findById(contactFromDb.getId());

        assertEquals(contactFromDb.getCommittees().size(),committees.size());

        Committee committeeFromDb = committeeService.findById(committee.getId());
        assertEquals(committeeFromDb.getMembers().iterator().next().getId(),contact.getId());

        /*Test remove committee*/

        committees.remove(newCommittee);

        contactFromDb.setCommittees(committees);
        contactService.update(contactFromDb);
        contactFromDb = contactService.findById(contact.getId());

        assertEquals(contactFromDb.getCommittees().size(), committees.size());
        assertEquals(contactFromDb.getCommittees().iterator().next().getId(), committee.getId());

    }

    @Test
    @Transactional
    public void testUpdateOrganizations () {

        Set<Contact> contacts = new HashSet<>();
        contacts.add(contact);


        Set<Organization> organizations = new HashSet<>();
        organizations.add(organization);
        contact.setOrganizations(organizations);
        organization.setMembers(contacts);
        organizationService.create(organization);

        contactService.create(contact);

        Contact contactFromDb = contactService.findById(contact.getId());

        /*Before*/
        assertEquals(contactFromDb.getOrganizations().size(), organizations.size());

        /*Test add new organization*/
        Organization newOrganization = new Organization();
        newOrganization.setName("new_committee");
        newOrganization.setMembers(contacts);
        organizationService.create(newOrganization);

        organizations.add(newOrganization);

        contactFromDb.setOrganizations(organizations);

        contactService.update(contactFromDb);

        contactFromDb = contactService.findById(contactFromDb.getId());

        assertEquals(contactFromDb.getOrganizations().size(), organizations.size());

        Organization organizationFromDb = organizationService.findById(organization.getId());
        assertEquals(organizationFromDb.getMembers().iterator().next().getId(),contact.getId());

        /*Test remove organization*/

        organizations.remove(newOrganization);

        contactFromDb.setOrganizations(organizations);
        contactService.update(contactFromDb);
        contactFromDb = contactService.findById(contact.getId());

        assertEquals(contactFromDb.getOrganizations().size(), organizations.size());
        assertEquals(contactFromDb.getOrganizations().iterator().next().getId(), organization.getId());

    }






}
