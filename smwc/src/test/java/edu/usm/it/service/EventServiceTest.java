package edu.usm.it.service;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Committee;
import edu.usm.domain.Contact;
import edu.usm.domain.Event;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.service.CommitteeService;
import edu.usm.service.ContactService;
import edu.usm.service.EncounterService;
import edu.usm.service.EventService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.Null;
import java.time.LocalDate;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.format;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

/**
 * Created by andrew on 8/26/15.
 */
public class EventServiceTest extends WebAppConfigurationAware {


    @Autowired
    EventService eventService;

    @Autowired
    CommitteeService committeeService;

    private Event event;
    private Committee committee;

    @Before
    public void setup() throws ConstraintViolation{

        committee = new Committee();
        committee.setName("Test Committee");
        committeeService.create(committee);

        event = new Event();
        event.setName("Test Event");
        event.setDateHeld(LocalDate.now().toString());
        event.setNotes("Some notes for the new test event");
        event.setLocation("Test Event Location");
        event.setCommittee(committee);

    }

    @After
    public void tearDown() throws NullDomainReference {
        eventService.deleteAll();
        committeeService.deleteAll();
    }

    @Test
    public void testCreateEvent() throws ConstraintViolation, NullDomainReference{
        String eventId = eventService.create(event);
        Event fromDb = eventService.findById(eventId);
        assertNotNull(fromDb);
        assertEquals(event.getName(), fromDb.getName());
    }

    @Test
    public void testCreateEventNullCommittee() throws ConstraintViolation, NullDomainReference {
        event.setCommittee(null);
        eventService.create(event);
    }

    @Test(expected = ConstraintViolation.class)
    public void testCreateEventNullName() throws ConstraintViolation, NullDomainReference {
        event.setName(null);
        eventService.create(event);
    }

    @Test(expected = ConstraintViolation.class)
    public void testCreateEventNullDate() throws ConstraintViolation, NullDomainReference {
        event.setDateHeld(null);
        eventService.create(event);
    }

    @Test
    public void testUpdateEvent() throws ConstraintViolation, NullDomainReference {
        eventService.create(event);
        event.setName("New Name");
        event.setCommittee(null);
        eventService.update(event);

        Event fromDb = eventService.findById(event.getId());
        assertEquals("New Name", fromDb.getName());
        assertNull(fromDb.getCommittee());
    }

    @Test(expected = ConstraintViolation.class)
    public void testUpdateEventNullName() throws ConstraintViolation, NullDomainReference {
        eventService.create(event);
        event.setName(null);
        eventService.update(event);
    }

    @Test(expected = ConstraintViolation.class)
    public void testUpdateEventNullDate() throws ConstraintViolation, NullDomainReference {
        eventService.create(event);
        event.setDateHeld(null);
        eventService.update(event);
    }

}
