package edu.usm.it.dao;

import edu.usm.domain.Committee;
import edu.usm.domain.Contact;
import edu.usm.config.WebAppConfigurationAware;
import edu.usm.repository.CommitteeDao;
import edu.usm.repository.ContactDao;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by scottkimball on 4/15/15.
 */
public class ContactCommitteesTest extends WebAppConfigurationAware {

    @Autowired
    CommitteeDao committeeDao;

    @Autowired
    ContactDao contactDao;

    private Committee committee;
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

        committee = new Committee();
        committee.setName("committee");




    }



    @Test
    @Transactional
    public void testSave() throws Exception {

        contactDao.save(contact);
        committeeDao.save(committee);

        /*Add committee to contact committee list and save*/
        Set<Committee> committees = new HashSet<>();
        committees.add(committee);
        contact.setCommittees(committees);
        contactDao.save(contact);

        /*Add contact to committee member list and save*/
        Set<Contact> contacts = new HashSet<>();
        contacts.add(contact);
        committee.setMembers(contacts);
        committeeDao.save(committee);


        /*test*/

        Contact contactFromDb = contactDao.findOne(contact.getId());
        assertNotNull(contactFromDb);
        assertEquals(contactFromDb.getCommittees().size(),1);

        Committee organizationFromDb = committeeDao.findOne(committee.getId());
        assertNotNull(organizationFromDb);
        assertEquals(organizationFromDb.getMembers().size(),1);

    }

    @Test
    @Transactional
    public void testDeleteOrganization() {

        contactDao.save(contact);
        committeeDao.save(committee);

        /*Add committee to contact committee list and save*/
        Set<Committee> committees = new HashSet<>();
        committees.add(committee);
        contact.setCommittees(committees);
        contactDao.save(contact);

        /*Add contact to committee member list and save*/
        Set<Contact> contacts = new HashSet<>();
        contacts.add(contact);
        committee.setMembers(contacts);
        committeeDao.save(committee);

        /*test delete committee*/
        contact.getCommittees().remove(committee);
        contactDao.save(contact);
        committeeDao.delete(committee);

        Contact contactFromDb = contactDao.findOne(contact.getId());
        assertNotNull(contactFromDb);
        assertEquals(contactFromDb.getCommittees().size(),0);
        assertNull(committeeDao.findOne(committee.getId()));




    }

    @Test
    @Transactional
    public void testDeleteContact() {

        contactDao.save(contact);
        committeeDao.save(committee);

        /*Add committee to contact committee list and save*/
        Set<Committee> committees = new HashSet<>();
        committees.add(committee);
        contact.setCommittees(committees);
        contactDao.save(contact);

        /*Add contact to committee member list and save*/
        Set<Contact> contacts = new HashSet<>();
        contacts.add(contact);
        committee.setMembers(contacts);
        committeeDao.save(committee);

        /*test delete committee*/
        committee.getMembers().remove(contact);
        committeeDao.save(committee);
        contactDao.delete(contact);

        Committee orgFromDb = committeeDao.findOne(committee.getId());
        assertNotNull(orgFromDb);
        assertEquals(orgFromDb.getMembers().size(),0);
        assertNull(contactDao.findOne(contact.getId()));


    }

}
