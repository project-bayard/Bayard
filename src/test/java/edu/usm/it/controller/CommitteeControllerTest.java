package edu.usm.it.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Committee;
import edu.usm.domain.Contact;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.dto.IdDto;
import edu.usm.service.CommitteeService;
import edu.usm.service.ContactService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by scottkimball on 6/5/15.
 */
public class CommitteeControllerTest extends WebAppConfigurationAware {

    @Autowired
    CommitteeService committeeService;

    @Autowired
    ContactService contactService;

    @Before
    public void setup() {

    }

    @After
    public void teardown() {
        committeeService.deleteAll();
        contactService.deleteAll();
    }

    @Test
    public void testGetAllCommittees() throws Exception {

        committeeService.deleteAll();
        contactService.deleteAll();

        Set<Contact> all = contactService.findAll();
        Assert.assertEquals(0, all.size());

        Contact contact = new Contact();
        contact.setFirstName("first");
        contact.setLastName("last");
        contact.setEmail("email@email.com");
        contactService.create(contact);

        Committee committee = new Committee();
        committee.setName("committeeName");
        committeeService.create(committee);
        contactService.addContactToCommittee(contact, committee);

        Set<Committee> allCommittees = committeeService.findAll();
        Assert.assertEquals(1, allCommittees.size());

        mockMvc.perform(get("/committees").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(committee.getId())))
                .andExpect(jsonPath("$.[0].name",is(committee.getName())));

    }

    @Test
    public void testCreateCommittee() throws Exception {

        committeeService.deleteAll();
        contactService.deleteAll();

        Committee committee = new Committee();
        committee.setName("committeeName");
        committee.setMembers(new HashSet<Contact>());
        String json = new ObjectMapper().writeValueAsString(committee);

        mockMvc.perform(post("/committees").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());

        Set<Committee> committees = committeeService.findAll();
        assertNotNull(committees);
        assertEquals(committees.size(),1);
        assertEquals(committees.iterator().next().getName(),committee.getName());

    }

    @Test
    public void testUpdateDetails() throws Exception {

        committeeService.deleteAll();
        contactService.deleteAll();

        Contact contact = new Contact();
        contact.setFirstName("first");
        contact.setLastName("last");
        contact.setEmail("email@email.com");
        contactService.create(contact);

        Committee committee = new Committee();
        committee.setName("name");
        String id = committeeService.create(committee);

        IdDto dto = new IdDto(id);
        String json = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(put("/contacts/"+contact.getId()+"/committees")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        committee.setName("newName");
        json = new ObjectMapper().writeValueAsString(committee);

        mockMvc.perform(put("/committees/" + committee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        mockMvc.perform(put("/committees/" + committee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        Set<Committee> committees = committeeService.findAll();
        assertNotNull(committees);
        assertEquals(1, committees.size());
        assertEquals("newName", committees.iterator().next().getName());
    }

    @Test
    public void testGetCommittee () throws Exception {

        committeeService.deleteAll();
        contactService.deleteAll();

        Contact contact = new Contact();
        contact.setFirstName("first");
        contact.setLastName("last");
        contact.setEmail("email@email.com");
        contactService.create(contact);

        Committee committee = new Committee();
        committee.setName("name");
        committeeService.create(committee);
        contactService.addContactToCommittee(contact, committee);

        mockMvc.perform(get("/committees/" + committee.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(committee.getId())))
                .andExpect(jsonPath("$.name", is(committee.getName())))
                .andExpect(jsonPath("$.members[0].id", is(contact.getId())))
                .andExpect(jsonPath("$.members[0].firstName", is(contact.getFirstName())))
                .andExpect(jsonPath("$.members[0].lastName", is(contact.getLastName())));
    }

    @Test(expected = NullDomainReference.NullCommittee.class)
    public void testDeleteCommittee () throws Exception {

        committeeService.deleteAll();
        contactService.deleteAll();

        Contact contact = new Contact();
        contact.setFirstName("first");
        contact.setLastName("last");
        contact.setEmail("email@email.com");
        contactService.create(contact);

        Committee committee = new Committee();
        committee.setName("name");
        String committeeId = committeeService.create(committee);
        contactService.addContactToCommittee(contact,committee);

        mockMvc.perform(delete("/committees/" + committeeId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Contact contactFromDb = contactService.findById(contact.getId());
        assertEquals(0, contactFromDb.getCommittees().size());

        Committee committeeFromDb = committeeService.findById(committeeId); // Should throw exception
    }
}
