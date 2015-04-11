package edu.usm.service;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Contact;
import edu.usm.domain.Organization;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


/**
 * Created by scottkimball on 3/12/15.
 */
public class ContactServiceTest extends WebAppConfigurationAware {

    @Autowired
    ContactService contactService;

    @Autowired
    OrganizationService organizationService;



    private Contact contact;
    private Contact contact2;
    private Organization organization;

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

    }


    @After
    public void teardown () {
        contactService.deleteAll();
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


        List<Contact> contacts = contactService.findAll();
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
        List<Contact> contacts = contactService.findAll();

        assertNotNull(contacts);
        assertEquals(contacts.size(),2);
    }

    @Test
    public void testDelete () {

        /*Test deleted from Organization member list*/
        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact);
        contacts.add(contact2);
        organization.setMembers(contacts);
        organizationService.create(organization);

        List<Organization> organizations = new ArrayList<>();
        organizations.add(organization);
        contact.setOrganizations(organizations);
        contactService.update(contact);
        contactService.delete(contact);

        Organization fromDb = organizationService.findById(organization.getId());

        assertNull(contactService.findById(contact.getId()));
        assertNotNull(fromDb);
        assertFalse(fromDb.getMembers().contains(contact));
        assertTrue(fromDb.getMembers().contains(contact2));

    }




}
