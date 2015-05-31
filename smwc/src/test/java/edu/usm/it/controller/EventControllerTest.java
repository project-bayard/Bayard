package edu.usm.it.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    ObjectWriter writer;

    @Before
    @Transactional
    public void setup() {
        writer = new ObjectMapper().writer();
    }

    private Event constructEvent(String name, String location, String date) {
        Event event = new Event();
        event.setName(name);
        event.setLocation(location);
        event.setDateHeld(date);
        return event;
    }

    private void persistDummyEvent() {
        //Attendee
        Contact attendee = new Contact();
        attendee.setFirstName("Test");
        attendee.setLastName("Attendee");
        contactService.create(attendee);

        //Event
        Event event = constructEvent("Rally", "Headquarters", "2012-01-01");

        Set<Contact> attendees = new HashSet<>();
        attendees.add(attendee);
        event.setAttendees(attendees);
        eventService.create(event);

        Set<Event> events = new HashSet<>();
        events.add(event);
        attendee.setAttendedEvents(events);
        contactService.update(attendee);
    }

    private void wipeData(){
        eventService.deleteAll();
    }

    @Test
    public void testGetAllEvents() throws Exception {

        wipeData();
        persistDummyEvent();

        Contact persistedAttendee = contactService.findAll().iterator().next();
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

    @Test
    public void testCreateEvent() throws Exception {

        Event newEvent = constructEvent("Test", "Church", "2012-01-01");
        String json = writer.writeValueAsString(newEvent);

        mockMvc.perform(post("/events").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());

    }

}
