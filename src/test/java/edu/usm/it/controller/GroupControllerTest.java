package edu.usm.it.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Aggregation;
import edu.usm.domain.Contact;
import edu.usm.domain.Group;
import edu.usm.domain.Organization;
import edu.usm.dto.GroupDto;
import edu.usm.service.ContactService;
import edu.usm.service.GroupService;
import edu.usm.service.OrganizationService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by andrew on 10/10/15.
 */
public class GroupControllerTest extends WebAppConfigurationAware {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private GroupService groupService;

    private Organization organization;
    private Contact contact;
    private Group group;

    @Before
    public void setup() {
        /*contact*/
        contact = new Contact();
        contact.setFirstName("first");
        contact.setLastName("last");
        contact.setEmail("email@email.com");

        /*orgs*/
        organization = new Organization();
        organization.setName("Org Name");
        organization.setMembers(new HashSet<>());
        organization.setStreetAddress("123 Organizational Lane");
        organization.setCity("Portland");
        organization.setState("ME");
        organization.setZipCode("04103");
        organization.setPhoneNumber("123-456-7890");
        organization.setPrimaryContactName("Theo McCeo");
        organization.setDescription("A very good organization");

        group = new Group();
        group.setGroupName("New Group");

    }

    @After
    public void tearDown() {
        groupService.deleteAll();
        contactService.deleteAll();
        organizationService.deleteAll();
    }

    @Test
    public void testPostGroup() throws Exception{

        String json = new ObjectMapper().writeValueAsString(group);
        mockMvc.perform(post("/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
        Set<Group> groups = groupService.findAll();
        assertEquals(groups.size(), 1);
        assertEquals(group.getGroupName(), groups.iterator().next().getGroupName());
    }

    @Test
    public void testUpdateGroupDetails() throws Exception {
        groupService.create(group);

        GroupDto dto = new GroupDto();
        String updateGroupName = "New Name";
        dto.setGroupName(updateGroupName);
        String json = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(put("/groups/"+ group.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        Group fromDb = groupService.findById(group.getId());
        assertEquals(updateGroupName, fromDb.getGroupName());

    }

    @Test
    public void testGetGroupById() throws Exception {
        groupService.create(group);

        mockMvc.perform(get("/groups/"+ group.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(group.getId())))
                .andExpect(jsonPath("$.groupName", is(group.getGroupName())))
                .andReturn();
    }

    @Test
    public void testGetAllGroups() throws Exception {
        groupService.create(group);

        mockMvc.perform(get("/groups")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(group.getId())))
                .andExpect(jsonPath("$.[0].groupName", is(group.getGroupName())))
                .andReturn();

    }

    @Test
    public void testDeleteGroup() throws Exception {
        groupService.create(group);

        mockMvc.perform(delete("/groups/"+ group.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Set<Group> groups = groupService.findAll();
        assertEquals(0, groups.size());
    }

    @Test
    public void testAddAggregation() throws Exception {
        groupService.create(group);
        organizationService.create(organization);
        contactService.create(contact);
        contactService.addContactToOrganization(contact.getId(), organization.getId());
        organization = organizationService.findById(organization.getId());

        String path = "/groups/"+group.getId()+"/aggregations/"+organization.getId();

        mockMvc.perform(put(path)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Group fromDb = groupService.findById(group.getId());
        assertEquals(1, fromDb.getAggregations().size());
        Aggregation aggregation = organizationService.findById(fromDb.getAggregations().iterator().next().getId());
        Aggregation orgFromGroup = organizationService.findById(aggregation.getId());
        int orgSize = organization.getMembers().size();
        int aggSize = orgFromGroup.getAggregationMembers().size();
        assertEquals(orgSize, aggSize);
        assertEquals(1, aggregation.getGroups().size());

    }

    @Test
    public void testRemoveAggregation() throws Exception {
        groupService.create(group);
        organizationService.create(organization);
        contactService.create(contact);
        contactService.addContactToOrganization(contact.getId(), organization.getId());
        groupService.addAggregation(organization.getId(), group.getId());

        group = groupService.findById(group.getId());
        assertEquals(1, group.getAggregations().size());

        mockMvc.perform(delete("/groups/"+group.getId()+"/aggregations/"+group.getAggregations().iterator().next().getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        group = groupService.findById(group.getId());
        assertEquals(0, group.getAggregations().size());

        Aggregation aggregation = organizationService.findById(organization.getId());
        assertEquals(0, aggregation.getGroups().size());

    }

    @Test
    public void testGetAllContactsInGroup() throws Exception {
        groupService.create(group);
        organizationService.create(organization);
        contactService.create(contact);
        Contact topLevel = new Contact();
        topLevel.setFirstName("Top Level");
        topLevel.setEmail("email@asd.com" );
        contactService.create(topLevel);

        contactService.addContactToOrganization(contact.getId(), organization.getId());
        groupService.addAggregation(organization.getId(), group.getId());
        contactService.addToGroup(topLevel.getId(), group.getId());

        String path = "/groups/"+group.getId()+"/all";

        mockMvc.perform(get(path)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }


}
