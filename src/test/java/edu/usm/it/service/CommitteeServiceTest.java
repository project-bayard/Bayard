package edu.usm.it.service;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Committee;
import edu.usm.domain.Contact;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.service.CommitteeService;
import edu.usm.service.ContactService;
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
 * Created by scottkimball on 4/15/15.
 */
public class CommitteeServiceTest extends WebAppConfigurationAware {

    @Autowired
    ContactService contactService;

    @Autowired
    CommitteeService committeeService;

    private Contact contact;
    private Contact contact2;
    private Committee committee;

    @Before
    public void setup() {
        committee = new Committee();
        committee.setName("committee");

        contact = new Contact();
        contact.setFirstName("First");
        contact.setLastName("Last");
        contact.setStreetAddress("123 Fake St");
        contact.setAptNumber("# 4");
        contact.setCity("Portland");
        contact.setZipCode("04101");
        contact.setEmail("email@gmail.com");

        contact2 = new Contact();
        contact2.setFirstName("Second");
        contact2.setLastName("Last");
        contact2.setStreetAddress("456 Fake St");
        contact2.setAptNumber("# 4");
        contact2.setCity("Lewiston");
        contact2.setZipCode("04108");
        contact2.setEmail("email@gmail.com");
    }

    @After
    public void tearDown() {
        committeeService.deleteAll();
        contactService.deleteAll();
    }

    @Test
    @Transactional
    public void testSave () throws Exception {
        contactService.create(contact);
        contactService.create(contact2);
        Set<Contact> contacts = new HashSet<>();
        contacts.add(contact);
        contacts.add(contact2);

        committee.setMembers(contacts);
        committeeService.create(committee);

        Committee committeeFromDb = committeeService.findById(committee.getId());

        assertNotNull(committeeFromDb);
        assertEquals(committeeFromDb.getMembers().size(), 2);
    }

    @Test
    public void testDelete () throws Exception {

        committeeService.create(committee);
        contactService.create(contact);
        contactService.create(contact2);

        contactService.addContactToCommittee(contact.getId(), committee.getId());
        contactService.addContactToCommittee(contact2.getId(), committee.getId());

        Contact contactFromDb = contactService.findById(contact.getId());
        Set<Committee> committeesFromDb = contactService.getAllContactCommittees(contactFromDb.getId());
        assertEquals(committeesFromDb.size(), 1); // before
        committeeService.delete(committee.getId());

        contactFromDb = contactService.findById(contact.getId()); // after
        committeesFromDb = contactService.getAllContactCommittees(contactFromDb.getId());
        assertNotNull(contactFromDb);
        assertEquals(committeesFromDb.size(), 0);

    }

    @Test
    public void testDeleteAll() throws Exception {
        committeeService.create(committee);
        contactService.create(contact);
        contactService.create(contact2);

        Committee another = new Committee();
        another.setName("Another Committee");
        committeeService.create(another);

        contactService.addContactToCommittee(contact.getId(), another.getId());
        contactService.addContactToCommittee(contact2.getId(), another.getId());

        Set<Committee> allCommittees = committeeService.findAll();
        assertEquals(2, allCommittees.size());

        committeeService.deleteAll();

        allCommittees = committeeService.findAll();
        assertEquals(0, allCommittees.size());

    }

    @Test(expected = ConstraintViolation.class)
    public void testCreateCommitteeNullName() throws ConstraintViolation {
        committee.setName(null);
        committeeService.create(committee);
    }



}
