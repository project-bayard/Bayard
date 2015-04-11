package edu.usm.dao;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Contact;
import edu.usm.domain.Organization;
import edu.usm.repository.ContactDao;
import edu.usm.repository.OrganizationDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by scottkimball on 4/11/15.
 */


public class ContactOrganizationsTest extends WebAppConfigurationAware {

    @Autowired
    OrganizationDao organizationDao;

    @Autowired
    ContactDao contactDao;

    private Organization organization;
    private Contact contact;

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
        contact.setAssessment(1);
        contact.setPhoneNumber1("phone number");
        contact.setInterests("interests");

        organization = new Organization();
        organization.setName("organization");




    }

    @After
    public void tearDown() {
        contactDao.deleteAll();
        organizationDao.deleteAll();
    }

    @Test
    @Transactional
    public void testSave() throws Exception {

        contactDao.save(contact);
        organizationDao.save(organization);

        /*Add organization to contact organization list and save*/
        List<Organization> organizationList = new ArrayList<>();
        organizationList.add(organization);
        contact.setOrganizations(organizationList);
        contactDao.save(contact);

        /*Add contact to organization member list and save*/
        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact);
        organization.setMembers(contacts);
        organizationDao.save(organization);


        /*test*/

        Contact contactFromDb = contactDao.findOne(contact.getId());
        assertNotNull(contactFromDb);
        assertEquals(contactFromDb.getOrganizations().size(),1);

        Organization organizationFromDb = organizationDao.findOne(organization.getId());
        assertNotNull(organizationFromDb);
        assertEquals(organizationFromDb.getMembers().size(),1);

    }

    @Test
    @Transactional
    public void testDeleteOrganization() {

        contactDao.save(contact);
        organizationDao.save(organization);

        /*Add organization to contact organization list and save*/
        List<Organization> organizationList = new ArrayList<>();
        organizationList.add(organization);
        contact.setOrganizations(organizationList);
        contactDao.save(contact);

        /*Add contact to organization member list and save*/
        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact);
        organization.setMembers(contacts);
        organizationDao.save(organization);

        /*test delete organization*/
        contact.getOrganizations().remove(organization);
        contactDao.save(contact);
        organizationDao.delete(organization);

        Contact contactFromDb = contactDao.findOne(contact.getId());
        assertNotNull(contactFromDb);
        assertEquals(contactFromDb.getOrganizations().size(),0);
        assertNull(organizationDao.findOne(organization.getId()));




    }

    @Test
    @Transactional
    public void testDeleteContact() {

        contactDao.save(contact);
        organizationDao.save(organization);

        /*Add organization to contact organization list and save*/
        List<Organization> organizationList = new ArrayList<>();
        organizationList.add(organization);
        contact.setOrganizations(organizationList);
        contactDao.save(contact);

        /*Add contact to organization member list and save*/
        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact);
        organization.setMembers(contacts);
        organizationDao.save(organization);

        /*test delete organization*/
        organization.getMembers().remove(contact);
        organizationDao.save(organization);
        contactDao.delete(contact);

        Organization orgFromDb = organizationDao.findOne(organization.getId());
        assertNotNull(orgFromDb);
        assertEquals(orgFromDb.getMembers().size(),0);
        assertNull(contactDao.findOne(contact.getId()));


    }


}
