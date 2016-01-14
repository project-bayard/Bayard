package edu.usm.it.service;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Committee;
import edu.usm.domain.Event;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.dto.EventDto;
import edu.usm.service.CommitteeService;
import edu.usm.service.EventService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertTrue;

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

    @Test(expected = ConstraintViolation.class)
    public void testCreateEventNonUnique() throws ConstraintViolation, NullDomainReference {
        eventService.create(event);
        Event duplicate = generateDuplicate(event);
        eventService.create(duplicate);
    }

    @Test(expected = ConstraintViolation.class)
    public void testUpdateEventNonUnique() throws ConstraintViolation, NullDomainReference {
        eventService.create(event);
        Event duplicate = generateDuplicate(event);
        duplicate.setName("Initially unique name");
        eventService.create(duplicate);

        duplicate.setName(event.getName());
        eventService.update(duplicate);
    }

    @Test
    public void removeCommitteeFromEvent() throws Exception {
        eventService.create(event);
        committee = committeeService.findById(committee.getId());
        assertTrue(!committee.getEvents().isEmpty());

        EventDto dto = new EventDto();
        dto.setCommitteeId("");
        dto.setDateHeld(event.getDateHeld());
        dto.setName(event.getName());
        dto.setLocation(event.getLocation());
        dto.setNotes(event.getNotes());

        eventService.update(event, dto);

        event = eventService.findById(event.getId());
        assertNull(event.getCommittee());

        committee = committeeService.findById(committee.getId());
        assertTrue(committee.getEvents().isEmpty());

    }

    private Event generateDuplicate(Event event) {
        Event duplicate = new Event();
        duplicate.setDateHeld(event.getDateHeld());
        duplicate.setName(event.getName());
        duplicate.setNotes("Unique notes");
        duplicate.setLocation("Unique location");
        return duplicate;
    }

}
