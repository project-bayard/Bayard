package edu.usm.it.dao;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Contact;
import edu.usm.domain.Encounter;
import edu.usm.domain.EncounterType;
import edu.usm.repository.ContactDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.SortedSet;
import java.util.TreeSet;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by scottkimball on 5/13/15.
 */
public class ContactEncountersTest extends WebAppConfigurationAware {

    @Autowired
    ContactDao contactDao;

    private Contact contact;
    private Encounter encounter;
    private Contact initiator;

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
        contactDao.save(contact);

        initiator = new Contact();
        initiator.setFirstName("Initiator");
        initiator.setEmail("email@email.com");
        initiator.setInitiator(true);
        contactDao.save(initiator);

        encounter = new Encounter();
        encounter.setAssessment(0);
        encounter.setEncounterDate(LocalDate.now().toString());
        encounter.setNotes("Notes");

        EncounterType encounterType = new EncounterType("CALL");
        encounter.setType(encounterType.getName());



    }

    @After
    public void teardown() {
        contactDao.deleteAll();
    }


    @Test
    @Transactional
    public void testSaveEncounter() {
        createEncounter(contact, initiator, encounter);
        Contact fromDb = contactDao.findOne(contact.getId());
        assertNotNull(fromDb.getEncounters());
        assertEquals(fromDb.getEncounters().size(), 1);
        assertEquals(fromDb.getEncounters().first().getAssessment(), encounter.getAssessment());
        assertNotNull(fromDb.getEncounters().first().getInitiator());
        assertEquals(fromDb.getEncounters().first().getInitiator().getId(), initiator.getId());

    }

    @Test
    @Transactional
    public void testDeleteContact() {
        createEncounter(contact, initiator, encounter);
        contactDao.delete(contact);
        Contact fromDb = contactDao.findOne(initiator.getId());
        assertNotNull(fromDb);
    }

    private void createEncounter(Contact contact, Contact initiator, Encounter encounter) {
        encounter.setInitiator(initiator);
        encounter.setContact(contact);
        SortedSet<Encounter> encountersInitiated = new TreeSet<>();
        encountersInitiated.add(encounter);
        initiator.setEncountersInitiated(encountersInitiated);
        contact.getEncounters().add(encounter);
        contactDao.save(contact);
        contactDao.save(initiator);
    }


    @Test(expected = Exception.class)
    public void testCreateEncounterNullContact() {
        createEncounter(contact, initiator, encounter);
        Encounter encounter = contact.getEncounters().first();
        encounter.setContact(null);

        contactDao.save(contact);
    }

    @Test(expected = Exception.class)
    public void testCreateEncounterNullDate() {
        createEncounter(contact, initiator, encounter);
        Encounter encounter = contact.getEncounters().first();
        encounter.setEncounterDate(null);

        contactDao.save(contact);
    }

    @Test(expected = Exception.class)
    public void testCreateEncounterNullType() {
        createEncounter(contact, initiator, encounter);
        Encounter encounter = contact.getEncounters().first();
        encounter.setType(null);

        contactDao.save(contact);
    }
}
