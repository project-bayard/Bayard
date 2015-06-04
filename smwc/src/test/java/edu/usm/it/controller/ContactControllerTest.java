package edu.usm.it.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usm.config.DateFormatConfig;
import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.*;
import edu.usm.dto.ContactDto;
import edu.usm.dto.EncounterDto;
import edu.usm.dto.OrganizationDto;
import edu.usm.mapper.ContactDtoMapper;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
        ContactDto contactDto = new ContactDto();

        contactDto.setFirstName("firstName");
        contactDto.setLastName("lastName");

        OrganizationDto organizationDto = new OrganizationDto();
        organizationDto.setName("newOrganization");
        Set<String> members = new HashSet<>();
        members.add(contactDto.getId());
        organizationDto.setMembers(members);

        Set<OrganizationDto> organizationDtoSet = new HashSet<>();
        organizationDtoSet.add(organizationDto);
        contactDto.setOrganizations(organizationDtoSet);

        /*Encounters*/
        EncounterDto encounterDto = new EncounterDto();
        encounterDto.setType(EncounterType.EVENT);
        encounterDto.setEncounterDate(dateFormatConfig.formatDomainDate(LocalDate.now()));
        encounterDto.setNotes("notes");
        encounterDto.setInitiator(initiator.getId());
        List<EncounterDto> encounterDtos = new ArrayList<>();
        encounterDtos.add(encounterDto);
        contactDto.setEncounters(encounterDtos);

        String json = new ObjectMapper().writeValueAsString(contactDto);

        mockMvc.perform(post("/contacts").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());

        Set<Contact> fromDb = contactService.findAll();
        assertEquals(fromDb.size(), 2);


    }


    @Test
    @Transactional
    public void testGetAllContacts () throws Exception {


        contactService.create(contact);
        String id = contact.getId();

        mockMvc.perform(get("/contacts").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    @Transactional
    public void testPutContact() throws Exception {
        contactService.create(contact);

        ContactDto contactDto = contactDtoMapper.convertToContactDto(contactService.findById(contact.getId()));
        assertNotNull(contactDto);

        EncounterDto encounterDto = new EncounterDto();
        encounterDto.setNotes("Notes for a new encounter");
        encounterDto.setAssessment(9);
        encounterDto.setContact(contactDto.getId());
        encounterDto.setInitiator(contactDto.getId());
        encounterDto.setType("Form");
        encounterDto.setEncounterDate(dateFormatConfig.formatDomainDate(LocalDate.now()));

        List<EncounterDto> encounters = new ArrayList<>();
        encounters.add(encounterDto);
        contactDto.setEncounters(encounters);

        String json = new ObjectMapper().writeValueAsString(contactDto);

        mockMvc.perform(put("/contacts/contact/" + contactDto.getId())
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        Contact fromDb = contactService.findById(contactDto.getId());
        assertNotNull(fromDb.getEncounters());
        assertNotNull(fromDb.getEncounters().get(0));

        Encounter persistedEncounter = fromDb.getEncounters().get(0);
        assertEquals(9, persistedEncounter.getAssessment());
        assertEquals(contactDto.getId(), persistedEncounter.getInitiator().getId());
        assertEquals("Form", persistedEncounter.getType());

    }


    @Test
    @Transactional
    public void testGetContact() throws Exception {

        contactService.create(contact);

        Set<Contact> contacts = new HashSet<>();
        contacts.add(contact);

        /*Event*/
        Event event = new Event();
        event.setDateHeld(dateFormatConfig.formatDomainDate(LocalDate.now()));
        event.setLocation("location");
        event.setNotes("notes");

        event.setAttendees(contacts);
        eventService.create(event);

        Set<Event> eventList = new HashSet<>();
        eventList.add(event);
        contact.setAttendedEvents(eventList);


        /*Member Info */
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setStatus(0);
        memberInfo.setPaidDues(true);
        memberInfo.setSignedAgreement(true);
        contact.setMemberInfo(memberInfo);



        /*Organization*/
        Organization organization = new Organization();
        organization.setName("organization");
        organization.setMembers(contacts);
        organizationService.create(organization);
        Set<Organization> organizations = new HashSet<>();
        organizations.add(organization);
        contact.setOrganizations(organizations);

        contactService.update(contact);


        mockMvc.perform(get("/contacts/contact/" + contact.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(contact.getId())))
                .andExpect(jsonPath("$.lastName", is(contact.getLastName())))
                .andExpect(jsonPath("$.streetAddress", is(contact.getStreetAddress())))
                .andExpect(jsonPath("$.aptNumber", is(contact.getAptNumber())))
                .andExpect(jsonPath("$.city", is(contact.getCity())))
                .andExpect(jsonPath("$.zipCode", is(contact.getZipCode())))
                .andExpect(jsonPath("$.email", is(contact.getEmail())))
                .andExpect(jsonPath("$.attendedEvents[0].id", is(contact.getAttendedEvents().iterator().next().getId())))
                .andExpect(jsonPath("$.memberInfo.id", is(contact.getMemberInfo().getId())))
                .andExpect(jsonPath("$.organizations[0].id", is(contact.getOrganizations().iterator().next().getId())));

    }

}
