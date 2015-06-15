package edu.usm.it.dao;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Contact;
import edu.usm.domain.Event;
import edu.usm.repository.ContactDao;
import edu.usm.repository.EventDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Andrew on 6/13/2015.
 */

public class ContactEventsTest extends WebAppConfigurationAware{

    @Autowired
    private EventDao eventDao;

    @Autowired
    private ContactDao contactDao;

    private Contact contact;

    private Event event;

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

        event = new Event();
        event.setName("Test Event");
    }

    @After
    public void tearDown() {
        contactDao.deleteAll();
        eventDao.deleteAll();
    }

    //Cannot be marked as transactional in order for join-table to be populated / hibernate cascading to occur
    @Test
    public void testAttendEvent() throws Exception {
        contactDao.save(contact);
        eventDao.save(event);

        contact.setAttendedEvents(new HashSet<>());
        contact.getAttendedEvents().add(event);
        contactDao.save(contact);

        Event persistedEvent = eventDao.findOne(event.getId());
        assertNotNull(persistedEvent.getAttendees());
        assertEquals(1, persistedEvent.getAttendees().size());

    }

    @Test
    public void testAttendDuplicateEvent() throws Exception {
        tearDown();
        setup();

        contactDao.save(contact);
        eventDao.save(event);

        contact.setAttendedEvents(new HashSet<>());
        contact.getAttendedEvents().add(event);
        contactDao.save(contact);

        contact.getAttendedEvents().add(event);
        contactDao.save(contact);

        contact = contactDao.findOne(contact.getId());
        assertEquals(1, contact.getAttendedEvents().size());

    }

    @Test
    public void testAddContactToEvent() throws Exception {
        tearDown();
        setup();

        eventDao.save(event);
        contactDao.save(contact);

        event.setAttendees(new HashSet<>());
        event.getAttendees().add(contact);
        eventDao.save(event);

        contact = contactDao.findOne(contact.getId());
        assertNotNull(contact.getAttendedEvents());
        assertEquals(0, contact.getAttendedEvents().size());

    }


}
