package edu.usm.it.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.*;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.dto.DtoTransformer;
import edu.usm.dto.EventDto;
import edu.usm.service.CommitteeService;
import edu.usm.service.ContactService;
import edu.usm.service.DonationService;
import edu.usm.service.EventService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.HashSet;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Andrew on 5/30/2015.
 */
public class EventControllerTest extends WebAppConfigurationAware {

    @Autowired
    private ContactService contactService;

    @Autowired
    private EventService eventService;

    @Autowired
    private CommitteeService committeeService;

    @Autowired
    private DonationService donationService;

    Event event;
    BudgetItem budgetItem;
    Donation donation;
    Contact contact;

    private ObjectWriter writer = new ObjectMapper().writer();


    @Before
    public void setup() {
        event = new Event();
        event.setName("Test Event");
        event.setLocation("Test Location");
        event.setDateHeld(LocalDate.now().toString());

        contact = new Contact();
        contact.setFirstName("Test");
        contact.setEmail("test@email.com");

        budgetItem = new BudgetItem("Test Budget Item");
        donationService.createBudgetItem(budgetItem);

        donation = new Donation();
        donation.setBudgetItem(budgetItem);
        donation.setAmount(200);
        donation.setDateOfDeposit(LocalDate.now());
        donation.setDateOfDeposit(LocalDate.of(2015, 1, 1));
    }

    @After
    public void tearDown(){
        eventService.deleteAll();
        committeeService.deleteAll();
        contactService.deleteAll();
        donationService.deleteAllBudgetItems();
        donationService.deleteAll();
    }


    @Test
    public void testGetAllEvents() throws Exception {

        eventService.create(event);
        contactService.create(contact);
        contactService.attendEvent(contact.getId(), event.getId());

        event = eventService.findAll().iterator().next();

        MvcResult result = mockMvc.perform(get("/events").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(event.getId())))
                .andExpect(jsonPath("$.[0].name", is(event.getName())))
                .andExpect(jsonPath("$.[0].location", is(event.getLocation())))
                .andExpect(jsonPath("$.[0].dateHeld", is(event.getDateHeld())))
                .andReturn();

    }

    @Test
    public void testCreateEvent() throws Exception {

        String json = writer.writeValueAsString(event);

        mockMvc.perform(post("/events").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());

    }

    @Test(expected = NullDomainReference.NullEvent.class)
    public void testDeleteEvent() throws Exception {
        String eventId = eventService.create(event);

        //Attendee
        Contact attendee = new Contact();
        attendee.setFirstName("Test");
        attendee.setLastName("Attendee");
        attendee.setEmail("email@email.com");
        contactService.create(attendee);
        attendee.setAttendedEvents(new HashSet<>());
        contactService.attendEvent(attendee.getId(), event.getId());

        mockMvc.perform(delete("/events/"+eventId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Event eventFromDb = eventService.findById(eventId);

        Contact contactFromDb = contactService.findById(attendee.getId());
        assertEquals(0, contactFromDb.getAttendedEvents().size());

    }

    @Test
    public void testUpdateEventPrimitives() throws Exception {
        String eventId = eventService.create(event);

        //Attendee
        Contact attendee = new Contact();
        attendee.setFirstName("Test");
        attendee.setLastName("Attendee");
        attendee.setEmail("email@email.com");
        contactService.create(attendee);
        attendee.setAttendedEvents(new HashSet<>());
        contactService.attendEvent(attendee.getId(), event.getId());

        EventDto dto = new EventDto();
        dto.setName("Updated Name");
        dto.setLocation(event.getLocation());
        dto.setNotes(event.getNotes());
        dto.setDateHeld(event.getDateHeld());

        String json = writer.writeValueAsString(dto);

        MvcResult result = mockMvc.perform(put("/events/" + eventId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Event eventFromDb = eventService.findById(eventId);
        assertTrue(eventService.getAllAttendees(eventId).contains(attendee));
        assertEquals("Updated Name", eventFromDb.getName());

        Contact contactFromDb = contactService.findById(attendee.getId());
        assertTrue(contactService.getAllContactEvents(contactFromDb.getId()).contains(eventFromDb));

    }

    @Test
    public void testUpdateChangedCommittee() throws Exception {

        //Old Committee
        Committee committee = new Committee();
        committee.setName("Test Event Committee");
        committeeService.create(committee);
        event.setCommittee(committee);
        String eventId = eventService.create(event);

        //Attendee
        Contact attendee = new Contact();
        attendee.setFirstName("Test");
        attendee.setLastName("Attendee");
        attendee.setEmail("email@email.com");
        contactService.create(attendee);
        attendee.setAttendedEvents(new HashSet<>());
        contactService.attendEvent(attendee.getId(), event.getId());

        //New Committee
        Committee newCommittee = new Committee();
        newCommittee.setName("New Committee");
        String newCommitteeId = committeeService.create(newCommittee);

        EventDto dto = new EventDto();
        dto.setName("Updated Name");
        dto.setLocation(event.getLocation());
        dto.setNotes(event.getNotes());
        dto.setDateHeld(event.getDateHeld());
        dto.setCommitteeId(newCommitteeId);

        String json = writer.writeValueAsString(dto);

        MvcResult result = mockMvc.perform(put("/events/" + eventId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Event eventFromDb = eventService.findById(eventId);
        assertTrue(eventService.getAllAttendees(eventId).contains(attendee));
        assertEquals("Updated Name", eventFromDb.getName());
        Committee committeeFromDb = eventService.getEventCommittee(eventId);
        assertEquals("New Committee", committeeFromDb.getName());
        Contact contactFromDb = contactService.findById(attendee.getId());
        assertTrue(contactService.getAllContactEvents(contactFromDb.getId()).contains(eventFromDb));

    }

    @Test
    public void testAddDonation() throws Exception {
        eventService.create(event);
        BayardTestUtilities.performEntityPost("/events/" + event.getId() + "/donations", DtoTransformer.fromEntity(donation), mockMvc);

        event = eventService.findById(event.getId());
        assertFalse(event.getDonations().isEmpty());
        assertEquals(budgetItem.getName(), event.getDonations().iterator().next().getBudgetItemName());
    }

    @Test
    public void testRemoveDonation() throws Exception {
        eventService.create(event);
        eventService.addDonation(event.getId(), DtoTransformer.fromEntity(donation));
        event = eventService.findById(event.getId());
        donation = event.getDonations().iterator().next();
        assertNotNull(donation);

        BayardTestUtilities.performEntityDelete("/events/" + event.getId() + "/donations/" + donation.getId(), mockMvc);
        event = eventService.findById(event.getId());
        assertTrue(event.getDonations().isEmpty());

        donation = donationService.findById(donation.getId());
        assertNotNull(donation);


    }

}
