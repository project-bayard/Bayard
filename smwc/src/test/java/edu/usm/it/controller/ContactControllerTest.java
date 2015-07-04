package edu.usm.it.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usm.config.DateFormatConfig;
import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Committee;
import edu.usm.domain.Contact;
import edu.usm.domain.Event;
import edu.usm.domain.Organization;
import edu.usm.dto.EncounterDto;
import edu.usm.dto.IdDto;
import edu.usm.dto.Response;
import edu.usm.mapper.ContactDtoMapper;
import edu.usm.service.CommitteeService;
import edu.usm.service.ContactService;
import edu.usm.service.EventService;
import edu.usm.service.OrganizationService;
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
import static org.junit.Assert.*;
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
    CommitteeService committeeService;

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
        contact.setDisabled(true);
        contact.setGender("Female");
        contact.setDateOfBirth(dateFormatConfig.formatDomainDate(LocalDate.now()));
        contact.setIncomeBracket("100K");
        contact.setEthnicity("White American");
        contact.setRace("Hispanic");
        contact.setSexualOrientation("Heterosexual");

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
    public void testAddContactToOrganization () throws Exception {
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


        /*Bad Contact Id*/
        mockMvc.perform(put("/contacts/" + "badId" + "/organizations")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk()).andExpect(jsonPath("status", is("FAILURE")));


        /*Bad org ID*/
        organizationIdDto.setId("badId");
        json = mapper.writeValueAsString(organizationIdDto);
        mockMvc.perform(put(path)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk()).andExpect(jsonPath("status", is("FAILURE")));
    }

    @Test
    public void testGetAllContactOrganizations () throws Exception {
        Organization organization = new Organization();
        organization.setName("orgName");

        contactService.create(contact);
        organizationService.create(organization);
        contactService.addContactToOrganization(contact,organization);

        mockMvc.perform(get("/contacts/" + contact.getId() + "/organizations")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id", is(organization.getId()))).andExpect(jsonPath("[0].name",
                is(organization.getName())));
    }

    @Test
    public void testRemoveContactFromOrganization () throws Exception {
        String id = contactService.create(contact);

        Organization organization = new Organization();
        organization.setName("orgName");

        String orgID = organizationService.create(organization);


        IdDto organizationIdDto = new IdDto(orgID);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(organizationIdDto);

        mockMvc.perform(delete("/contacts/" + id + "/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("SUCCESS")));
    }

    @Test
    @Transactional
    public void testCreateEncounter() throws Exception {
        String id = contactService.create(contact);
        String initiatorId = contactService.create(generateSecondcontact());
        EncounterDto dto = new EncounterDto();
        dto.setInitiatorId(initiatorId);
        dto.setNotes("Notes");
        dto.setAssessment(9);
        dto.setType("Call");
        dto.setDate(dateFormatConfig.formatDomainDate(LocalDate.now()));

        String json = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(put("/contacts/"+id+"/encounters")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("SUCCESS")));

    }

    @Test
    @Transactional
    public void testGetDemographicDetails() throws Exception {
        String id = contactService.create(contact);

        mockMvc.perform(get("/contacts/" + id + "/demographics")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.race", is(contact.getRace())))
                .andExpect(jsonPath("$.ethnicity", is(contact.getEthnicity())))
                .andExpect(jsonPath("$.dateOfBirth", is(contact.getDateOfBirth())))
                .andExpect(jsonPath("$.gender", is(contact.getGender())))
                .andExpect(jsonPath("$.disabled", is(contact.isDisabled())))
                .andExpect(jsonPath("$.incomeBracket", is(contact.getIncomeBracket())))
                .andExpect(jsonPath("$.sexualOrientation", is(contact.getSexualOrientation())));
    }
    public void testAddContactToCommittee () throws  Exception {
        String id = contactService.create(contact);

        Committee committee = new Committee();
        committee.setName("committee name");
        String committeeId = committeeService.create(committee);

        IdDto committeeIdDto = new IdDto(committeeId);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(committeeIdDto);
        String path = "/contacts/" + id + "/committees";

        mockMvc.perform(put(path)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        Contact fromDb = contactService.findById(contact.getId());
        Committee committeeFromDb = committeeService.findById(committee.getId());

        assertNotNull(fromDb);
        assertEquals(fromDb.getCommittees().iterator().next().getId(), committee.getId());
        assertNotNull(committeeFromDb);
        assertEquals(committeeFromDb.getMembers().iterator().next().getId(), contact.getId());


        /*Bad Contact Id*/
        mockMvc.perform(put("/contacts/" + "badId" + "/committees")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk()).andExpect(jsonPath("status", is("FAILURE")));


        /*Bad org ID*/
        committeeIdDto.setId("badId");
        json = mapper.writeValueAsString(committeeIdDto);
        mockMvc.perform(put(path)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk()).andExpect(jsonPath("status", is("FAILURE")));

    }

    @Test
    @Transactional
    public void testUpdateDemographicDetails() throws Exception {
        String id = contactService.create(contact);

        Contact contactDetails = new Contact();
        contactDetails.setIncomeBracket("High");
        contactDetails.setRace("Race");
        contactDetails.setSexualOrientation("Other");
        contactDetails.setDisabled(false);
        contactDetails.setEthnicity("Other");
        contactDetails.setDateOfBirth(dateFormatConfig.formatDomainDate(LocalDate.now()));
        contactDetails.setGender("F");

        String json = new ObjectMapper().writeValueAsString(contactDetails);

        mockMvc.perform(put("/contacts/" + id + "/demographics")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(Response.SUCCESS)));
    }

    public void testRemoveContactFromCommittee () throws Exception {
        String id = contactService.create(contact);

        Committee committee = new Committee();
        committee.setName("orgName");

        String committeeID = committeeService.create(committee);


        IdDto organizationIdDto = new IdDto(committeeID);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(organizationIdDto);

        mockMvc.perform(delete("/contacts/" + id + "/committees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("SUCCESS")));
    }

    @Test
    public void testGetAllContactCommittees () throws Exception {
        Committee committee = new Committee();
        committee.setName("orgName");

        contactService.create(contact);
        committeeService.create(committee);
        contactService.addContactToCommittee(contact, committee);

        mockMvc.perform(get("/contacts/" + contact.getId() + "/committees")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id", is(committee.getId()))).andExpect(jsonPath("[0].name",
                is(committee.getName())));
    }

    private Contact generateSecondcontact() {
        Contact secondContact = new Contact();
        secondContact.setFirstName("Second");
        secondContact.setLastName("ToLast");
        secondContact.setStreetAddress("541 Downtown Abbey");
        secondContact.setAptNumber("# 9");
        secondContact.setCity("Yarmouth");
        secondContact.setZipCode("04096");
        secondContact.setEmail("second@gmail.com");
        return secondContact;
    }
}
