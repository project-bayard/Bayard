package edu.usm.it.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Committee;
import edu.usm.domain.Contact;
import edu.usm.service.CommitteeService;
import edu.usm.service.ContactService;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.transaction.Transactional;
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

    @After
    public void teardown() {
        committeeService.deleteAll();
        contactService.deleteAll();
    }
    @Test
    @Transactional
    public void testGetAllCommittees() throws Exception {

        Contact contact = new Contact();
        contact.setFirstName("first");
        contact.setLastName("last");
        contactService.create(contact);


        Committee committee = new Committee();
        committee.setName("committeeName");
        Set<Contact> members = new HashSet<>();
        members.add(contact);
        committee.setMembers(members);
        committeeService.create(committee);

        Set<Committee> committees = new HashSet<>();
        committees.add(committee);
        contact.setCommittees(committees);
        contactService.update(contact);

        mockMvc.perform(get("/committees").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(committee.getId())))
                .andExpect(jsonPath("$.[0].name",is(committee.getName())))
                .andExpect(jsonPath("$.[0].members[0].id",is(contact.getId())))
                .andExpect(jsonPath("$.[0].members[0].firstName", is(contact.getFirstName())))
                .andExpect(jsonPath("$.[0].members[0].lastName", is(contact.getLastName())));

    }

    @Test
    @Transactional
    public void testCreateCommittee() throws Exception {
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
    @Transactional
    public void testUpdateCommittee() throws Exception {
        Committee committee = new Committee();
        committee.setName("name");
        committeeService.create(committee);

        committee.setName("newName");
        String json = new ObjectMapper().writeValueAsString(committee);

        mockMvc.perform(put("/committees/" + committee.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        Set<Committee> committees = committeeService.findAll();
        assertNotNull(committees);
        assertEquals(committees.size(),1);
        assertEquals(committees.iterator().next().getName(),committee.getName());
    }

    @Test
    @Transactional
    public void testGetCommittee () throws Exception {
        Contact contact = new Contact();
        contact.setFirstName("first");
        contact.setLastName("last");
        contactService.create(contact);

        Committee committee = new Committee();
        committee.setName("name");
        Set<Contact> members = new HashSet<>();
        members.add(contact);
        committee.setMembers(members);
        committeeService.create(committee);

        Set<Committee> committees = new HashSet<>();
        committees.add(committee);
        contact.setCommittees(committees);
        contactService.update(contact);

        mockMvc.perform(get("/committees/" + committee.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(committee.getId())))
                .andExpect(jsonPath("$.name", is(committee.getName())))
                .andExpect(jsonPath("$.members[0].id", is(contact.getId())))
                .andExpect(jsonPath("$.members[0].firstName", is(contact.getFirstName())))
                .andExpect(jsonPath("$.members[0].lastName", is(contact.getLastName())));
    }
}
