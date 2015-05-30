package edu.usm.it.controller;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Contact;
import edu.usm.domain.Event;
import edu.usm.service.ContactService;
import edu.usm.service.EventService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Andrew on 5/30/2015.
 */
public class EventControllerTest extends WebAppConfigurationAware {

    @Autowired
    ContactService contactService;

    @Autowired
    EventService eventService;

    @Before
    @Transactional
    public void setup() {

        //Attendee
        Contact attendee = new Contact();
        attendee.setFirstName("Test");
        attendee.setLastName("Attendee");
        contactService.create(attendee);

        //Event
        Event event = new Event();
        event.setDateHeld("2012-01-01");
        event.setLocation("Headquarters");
        event.setName("Rally");

        Set<Contact> attendees = new HashSet<>();
        attendees.add(attendee);
        event.setAttendees(attendees);
        eventService.create(event);

        Set<Event> events = new HashSet<>();
        events.add(event);
        attendee.setAttendedEvents(events);
        contactService.update(attendee);

    }

    @Test
    public void testGetAllEvents() throws Exception {


        Contact persistedAttendee = contactService.findAll().iterator().next();
        assertEquals(1, persistedAttendee.getAttendedEvents().size());

        Event event = eventService.findAll().iterator().next();

        MvcResult result = mockMvc.perform(get("/events").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(persistedAttendee.getAttendedEvents().iterator().next().getId())))
                .andExpect(jsonPath("$.[0].name", is(event.getName())))
                .andExpect(jsonPath("$.[0].location", is(event.getLocation())))
                .andExpect(jsonPath("$.[0].dateHeld", is(event.getDateHeld())))
                .andExpect(jsonPath("$.[0].attendees.[0].id", is(persistedAttendee.getId())))
                .andExpect(jsonPath("$.[0].attendees.[0].firstName", is(persistedAttendee.getFirstName())))
                .andExpect(jsonPath("$.[0].attendees.[0].lastName", is(persistedAttendee.getLastName())))
                .andReturn();

    }

}
