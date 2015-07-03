package edu.usm.it.service;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Contact;
import edu.usm.domain.Organization;
import edu.usm.service.ContactService;
import edu.usm.service.OrganizationService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by scottkimball on 4/11/15.
 */
public class OrganizationServiceTest extends WebAppConfigurationAware {
    @Autowired
    ContactService contactService;

    @Autowired
    OrganizationService organizationService;

    private Contact contact;
    private Contact contact2;
    private Organization organization;

    @Before
    public void setup() {
        organization = new Organization();
        organization.setName("organization");
        organization.setStreetAddress("123 Organizational Lane");
        organization.setCity("Portland");
        organization.setState("ME");
        organization.setZipCode("04103");
        organization.setPhoneNumber("123-456-7890");
        organization.setPrimaryContactName("Theo McCeo");
        organization.setDescription("A very good organization");

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
    }

    @After
    public void tearDown() {
        organizationService.deleteAll();
        contactService.deleteAll();

    }

    @Test
    @Transactional
    public void testSave () throws Exception {
        Set<Contact> contacts = new HashSet<>();
        contacts.add(contact);
        contacts.add(contact2);
        contactService.create(contact);
        contactService.create(contact2);
        Set<Organization> organizations = new HashSet<>();
        organizations.add(organization);
        organizationService.create(organization);


        organization.setMembers(contacts);
        organizationService.update(organization);

        contact.setOrganizations(organizations);
        contact2.setOrganizations(organizations);
        contactService.update(contact);
        contactService.update(contact2);
        Organization orgFromDb = organizationService.findById(organization.getId());

        assertNotNull(orgFromDb);
        assertEquals(orgFromDb.getMembers().size(),1);
        assertEquals(organization.getDescription(), orgFromDb.getDescription());
        assertEquals(organization.getName(), orgFromDb.getName());
        assertEquals(organization.getPhoneNumber(), orgFromDb.getPhoneNumber());
        assertEquals(organization.getPrimaryContactName(), orgFromDb.getPrimaryContactName());
    }

    @Test
    @Transactional
    public void testDelete () throws Exception {
        Set<Contact> contacts = new HashSet<>();
        contacts.add(contact);
        contacts.add(contact2);

        organization.setMembers(contacts);
        organizationService.create(organization);

        Set<Organization> organizations = new HashSet<>();
        organizations.add(organization);

        contact.setOrganizations(organizations);
        contact2.setOrganizations(organizations);


        contactService.update(contact);

        Contact contactFromDb = contactService.findById(contact.getId());
        assertEquals(contactFromDb.getOrganizations().size(),1); // before
        organizationService.deleteAll();

        contactFromDb = contactService.findById(contact.getId()); // after
        assertNotNull(contactFromDb);
        assertEquals(contactFromDb.getOrganizations().size(),0);

    }
}
