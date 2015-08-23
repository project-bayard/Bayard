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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by scottkimball on 5/13/15.
 */
public class ContactEncountersTest extends WebAppConfigurationAware {

    @Autowired
    ContactDao contactDao;

    private Contact contact;
    private Encounter encounter;

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

        encounter = new Encounter();
        encounter.setAssessment(0);
        encounter.setContact(contact);
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
        Contact initiator = new Contact();
        initiator.setFirstName("initiator");
        SortedSet<Encounter> encountersInitiated = new TreeSet<>();
        encountersInitiated.add(encounter);
        initiator.setEncountersInitiated(encountersInitiated);
        contactDao.save(initiator);



        encounter.setInitiator(initiator);
        SortedSet<Encounter> encounters = new TreeSet<>();
        encounters.add(encounter);
        contact.setEncounters(encounters);
        contactDao.save(contact);

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
        contactDao.save(contact);
        Contact initiator = new Contact();
        initiator.setFirstName("initiator");
        SortedSet<Encounter> encountersInitiated = new TreeSet<>();
        encountersInitiated.add(encounter);
        initiator.setEncountersInitiated(encountersInitiated);
        contactDao.save(initiator);

        encounter.setInitiator(initiator);
        SortedSet<Encounter> encounters = new TreeSet<>();
        encounters.add(encounter);
        contact.setEncounters(encounters);
        contactDao.save(contact);

        contactDao.delete(contact);
        Contact fromDb = contactDao.findOne(initiator.getId());
        assertNotNull(fromDb);



    }
}
