package edu.usm.it.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usm.config.DateFormatConfig;
import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Contact;
import edu.usm.domain.Event;
import edu.usm.domain.Organization;
import edu.usm.dto.IdDto;
import edu.usm.dto.Response;
import edu.usm.mapper.ContactDtoMapper;
import edu.usm.service.ContactService;
import edu.usm.service.EventService;
import edu.usm.service.OrganizationService;
import org.assertj.core.internal.cglib.core.Local;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by scottkimball on 3/12/15.
 */

public class ContactControllerTest extends WebAppConfigurationAware {


    @Autowired
    ContactService contactService;
    private Logger logger = LoggerFactory.getLogger(ContactControllerTest.class);

    @Autowired
    OrganizationService organizationService;

    @Autowired
    EventService eventService;

    @Autowired
    DateFormatConfig dateFormatConfig;

    @Autowired
    ContactDtoMapper contactDtoMapper;


    private Contact contact;
    private Contact initiator;
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

        initiator = new Contact();
        initiator.setFirstName("initiatorFirst");
        contactService.create(initiator);

        event = new Event();
        event.setName("Test Event");
        event.setLocation("Test event location");
        event.setDateHeld(dateFormatConfig.formatDomainDate(LocalDate.of(2015, 01, 01)));
        event.setNotes("Test event notes");
    }

    @After
    public void teardown () {

        organizationService.deleteAll();
        contactService.deleteAll();
        eventService.deleteAll();
    }


    @Test
    @Transactional
    public void testPostContact() throws Exception {
        String json = new ObjectMapper().writeValueAsString(contact);

        mockMvc.perform(post("/contacts").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(Response.SUCCESS)));

        Set<Contact> fromDb = contactService.findAll();
        assertEquals(fromDb.size(), 2);

    }


    @Test
    @Transactional
    public void testGetAllContacts () throws Exception {

        contactService.create(contact);
        mockMvc.perform(get("/contacts").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Transactional
    public void testPutContact() throws Exception {
        contactService.create(contact);

        contact.setFirstName("newFirstName");

        String json = new ObjectMapper().writeValueAsString(contact);

        mockMvc.perform(put("/contacts/" + contact.getId())
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(Response.SUCCESS)))
                .andExpect(jsonPath("$.id", is(contact.getId())));


    }


    @Test
    @Transactional
    public void testGetContact() throws Exception {

        contactService.create(contact);

        mockMvc.perform(get("/contacts/" + contact.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(contact.getId())))
                .andExpect(jsonPath("$.lastName", is(contact.getLastName())))
                .andExpect(jsonPath("$.streetAddress", is(contact.getStreetAddress())))
                .andExpect(jsonPath("$.aptNumber", is(contact.getAptNumber())))
                .andExpect(jsonPath("$.city", is(contact.getCity())))
                .andExpect(jsonPath("$.zipCode", is(contact.getZipCode())))
                .andExpect(jsonPath("$.email", is(contact.getEmail())));

    }

    @Test
    public void testGetAllInitiators() throws Exception {
        Contact contact = new Contact();
        contact.setFirstName("firstName");
        contact.setLastName("lastName");
        contact.setInitiator(true);

        contactService.create(contact);

        mockMvc.perform(get("/contacts/initiators")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id", is(contact.getId())));
    }

    @Test
    @Transactional
    public void testPutEvent() throws Exception {
        String contactId = contactService.create(contact);
        String eventId = eventService.create(event);

        IdDto eventIdDto = new IdDto(eventId);
        String requestBody = new ObjectMapper().writeValueAsString(eventIdDto);
        String path = "/contacts/"+contactId+"/events";

        mockMvc.perform(put(path)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());

        Contact contactFromDb = contactService.findById(contactId);
        Event eventFromDb = eventService.findById(eventId);

        assertNotNull(contactFromDb);
        assertNotNull(eventFromDb);

        assertEquals(1, contactFromDb.getAttendedEvents().size());
        assertTrue(contactFromDb.getAttendedEvents().contains(eventFromDb));

    }

    @Test
    @Transactional
    public void testAlternativeAddContactToOrganization () throws Exception {
        String id = contactService.create(contact);

        Organization organization = new Organization();
        organization.setName("org name");
        String orgId = organizationService.create(organization);

        IdDto organizationIdDto = new IdDto(orgId);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(organizationIdDto);
        String path = "/contacts/" + id + "/organizations";

        mockMvc.perform(put(path)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        Contact fromDb = contactService.findById(contact.getId());
        Organization orgFromDb = organizationService.findById(organization.getId());

        assertNotNull(fromDb);
        assertEquals(fromDb.getOrganizations().iterator().next().getId(),organization.getId());
        assertNotNull(orgFromDb);
        assertEquals(orgFromDb.getMembers().iterator().next().getId(), contact.getId());

    }

//    @Test
//    @Transactional
//    public void testAddContactToOrganization () throws Exception {
//        String id = contactService.create(contact);
//
//        Organization organization = new Organization();
//        organization.setName("org name");
//        String orgId = organizationService.create(organization);
//
//        IdDto idDto = new IdDto(orgId);
//        ObjectMapper mapper = new ObjectMapper();
//        String json = mapper.writeValueAsString(idDto);
//        String path = "/contacts/" + id + "/organizations/" + orgId;
//
//        mockMvc.perform(put(path)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status", is(Response.SUCCESS)));
//
//        Contact fromDb = contactService.findById(contact.getId());
//        Organization orgFromDb = organizationService.findById(organization.getId());
//
//        assertNotNull(fromDb);
//        assertEquals(fromDb.getOrganizations().iterator().next().getId(),organization.getId());
//        assertNotNull(orgFromDb);
//        assertEquals(orgFromDb.getMembers().iterator().next().getId(), contact.getId());
//
//    }
}
