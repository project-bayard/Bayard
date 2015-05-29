package edu.usm.it.controller;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Contact;
import edu.usm.domain.Encounter;
import edu.usm.domain.Organization;
import edu.usm.service.ContactService;
import edu.usm.service.OrganizationService;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by scottkimball on 5/28/15.
 */
public class OrganizationControllerTest extends WebAppConfigurationAware {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ContactService contactService;

    private Organization organization;
    private Contact contact;
    private Contact initiator;

    @After
    public void teardown() {
        organizationService.deleteAll();
    }


    @Test
    public void testGetAllOrganizations() throws Exception {
         /*Initiator*/
        initiator = new Contact();
        initiator.setFirstName("initiatorFirst");
        initiator.setLastName("initiatorLast");
        contactService.create(initiator);


        /*contact*/
        contact = new Contact();
        contact.setFirstName("first");
        contact.setLastName("last");


        Encounter encounter = new Encounter();
        encounter.setInitiator(initiator);
        encounter.setContact(contact);
        encounter.setAssessment(4);
        List<Encounter> encounterList = new ArrayList<>();
        encounterList.add(encounter);
        contact.setEncounters(encounterList);


        /*orgs*/
        organization = new Organization();
        organization.setName("Org Name");
        Set<Contact> members = new HashSet<>();
        members.add(contact);
        organization.setMembers(members);

        Set<Organization> organizations = new HashSet<>();
        organizations.add(organization);
        contact.setOrganizations(organizations);
        contactService.create(contact);


        MvcResult result = mockMvc.perform(get("/organizations").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(contact.getOrganizations().iterator().next().getId())))
                .andExpect(jsonPath("$.[0].name", is(organization.getName())))
                .andExpect(jsonPath("$.[0].members.[0].id", is(contact.getId())))
                .andExpect(jsonPath("$.[0].members.[0].firstName", is(contact.getFirstName())))
                .andExpect(jsonPath("$.[0].members.[0].assessment", is(contact.getAssessment())))
                .andReturn();

    }

    @Test
    public void testCreateOrganization() throws Exception {

        String json = "{\"name\" : \"orgName\", \"members\" : []}";
        mockMvc.perform(post("/organizations").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
        Set<Organization> organizations = organizationService.findAll();
        assertEquals(organizations.size(),1);
    }


}
