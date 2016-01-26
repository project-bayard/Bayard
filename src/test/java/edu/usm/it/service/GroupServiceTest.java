package edu.usm.it.service;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.*;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.service.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by andrew on 10/8/15.
 */
public class GroupServiceTest extends WebAppConfigurationAware{

    @Autowired
    private GroupService groupService;

    @Autowired
    private CommitteeService committeeService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private EventService eventService;

    private Group group;
    private Committee committee;
    private Organization organization;
    private Event event;

    private Contact first;
    private Contact second;
    private Contact topLevel;

    @Before
    public void setup() throws ConstraintViolation, NullDomainReference{

        createContacts();

        committee = new Committee();
        committee.setName("Committee Name");
        committeeService.create(committee);
        contactService.addContactToCommittee(first, committee);
        contactService.addContactToCommittee(second, committee);

        organization = new Organization();
        organization.setName("Organization Name");
        organizationService.create(organization);
        contactService.addContactToOrganization(first, organization);
        contactService.addContactToOrganization(second, organization);

        event = new Event();
        event.setName("Event Name");
        event.setDateHeld("2015-01-01");
        eventService.create(event);
        contactService.attendEvent(first, event);
        contactService.attendEvent(second, event);

        group = new Group();
        group.setGroupName("New AlinskyGroup");

    }

    private void createContacts() throws ConstraintViolation{
        first = new Contact();
        first.setFirstName("First");
        first.setEmail("email@emememe.com");

        second = new Contact();
        second.setFirstName("Second");
        second.setEmail("second@email.com");

        topLevel = new Contact();
        topLevel.setFirstName("Top Level");
        topLevel.setEmail("email@email.com");

        contactService.create(first);
        contactService.create(second);
        contactService.create(topLevel);
    }

    @After
    public void tearDown() {
        groupService.deleteAll();
        organizationService.deleteAll();
        committeeService.deleteAll();
        eventService.deleteAll();
        contactService.deleteAll();
    }

    @Test
    public void testCreateGroupFromCommittee() throws Exception{
        String id = groupService.create(group);
        groupService.addAggregation(committee, group);

        Group fromDb = groupService.findById(id);
        assertEquals(group.getGroupName(), fromDb.getGroupName());
        Aggregation aggregation = fromDb.getAggregations().iterator().next();
        assertNotNull(aggregation);
        assertEquals(committee.getAggregationMembers().size(), aggregation.getAggregationMembers().size());
    }

    @Test
    public void testCreateGroupFromOrganization() throws Exception{
        String id = groupService.create(group);
        groupService.addAggregation(organization, group);

        Group fromDb = groupService.findById(id);
        assertEquals(group.getGroupName(), fromDb.getGroupName());
        Aggregation aggregation = fromDb.getAggregations().iterator().next();
        assertNotNull(aggregation);
        assertEquals(organization.getAggregationMembers().size(), aggregation.getAggregationMembers().size());
    }

    @Test
    public void testCreateGroupFromEvent() throws Exception{
        String id = groupService.create(group);
        groupService.addAggregation(event, group);

        Group fromDb = groupService.findById(id);
        assertEquals(group.getGroupName(), fromDb.getGroupName());
        Aggregation aggregation = fromDb.getAggregations().iterator().next();
        assertNotNull(aggregation);
        assertEquals(1, aggregation.getGroups().size());
        assertEquals(event.getAggregationMembers().size(), aggregation.getAggregationMembers().size());
    }

    @Test
    public void testCreateGroupFromMultipleAggregations() throws Exception{
        String id = groupService.create(group);
        groupService.addAggregation(committee, group);
        group = groupService.findById(group.getId());
        groupService.addAggregation(organization, group);
        group = groupService.findById(group.getId());
        groupService.addAggregation(event, group);
        group = groupService.findById(group.getId());
        contactService.addToGroup(topLevel, group);

        Group fromDb = groupService.findById(id);
        assertEquals(group.getGroupName(), fromDb.getGroupName());
        assertEquals(3, group.getAggregations().size());
        Set<Contact> allAggregationMembers = new HashSet<>();
        for (Aggregation aggregation: group.getAggregations()) {
            for (Contact c: aggregation.getAggregationMembers()) {
                allAggregationMembers.add(c);
            }
        }
        assertEquals(2, allAggregationMembers.size());
        assertEquals(1, group.getTopLevelMembers().size());
    }

    @Test
    public void testDeleteGroup() throws Exception{
        groupService.create(group);
        groupService.addAggregation(committee, group);

        group = groupService.findById(group.getId());
        contactService.addToGroup(topLevel, group);

        groupService.delete(group);

        Group groupFromDb = groupService.findById(group.getId());
        assertNull(groupFromDb);
        Aggregation fromDb = committeeService.findById(committee.getId());
        assertNotNull(fromDb);
        assertEquals(0, fromDb.getGroups().size());
        assertEquals(committee.getAggregationMembers().size(), fromDb.getAggregationMembers().size());

        Contact topLevelFromDb = contactService.findById(topLevel.getId());
        assertNotNull(topLevelFromDb);
        assertEquals(0, topLevelFromDb.getGroups().size());

    }

    @Test
    public void testAddMultipleContacts() throws Exception{
        groupService.create(group);
        groupService.addAggregation(committee, group);

        group = groupService.findById(group.getId());
        contactService.addToGroup(topLevel, group);

        Contact anotherContact = new Contact();
        anotherContact.setFirstName("Another");
        anotherContact.setEmail("another@email.com");
        contactService.create(anotherContact);

        contactService.addToGroup(anotherContact, group);
        group = groupService.findById(group.getId());
        assertEquals(2, group.getTopLevelMembers().size());

        anotherContact = contactService.findById(anotherContact.getId());
        topLevel = contactService.findById(topLevel.getId());

        assertEquals(1, anotherContact.getGroups().size());
        assertEquals(1, topLevel.getGroups().size());

        groupService.delete(group);

        anotherContact = contactService.findById(anotherContact.getId());
        topLevel = contactService.findById(topLevel.getId());

        assertEquals(0, anotherContact.getGroups().size());
        assertEquals(0, topLevel.getGroups().size());
    }

    @Test
    public void testRemoveContactFromGroup() throws Exception {
        groupService.create(group);
        groupService.addAggregation(committee, group);
        contactService.addToGroup(topLevel, group);

        assertEquals(1, group.getTopLevelMembers().size());

        contactService.removeFromGroup(topLevel, group);

        Group fromDb = groupService.findById(group.getId());
        assertEquals(0, fromDb.getTopLevelMembers().size());

        Contact contactFromDb = contactService.findById(topLevel.getId());
        assertEquals(0, contactFromDb.getGroups().size());
    }

    @Test
    public void testAddAggregation() throws Exception {
        groupService.create(group);
        groupService.addAggregation(committee, group);

        Group fromDb = groupService.findById(group.getId());
        assertEquals(1, fromDb.getAggregations().size());
        Aggregation aggregation = fromDb.getAggregations().iterator().next();
        assertEquals(group.getGroupName(), aggregation.getGroups().iterator().next().getGroupName());
    }

    @Test
    public void testRemoveAggregation() throws Exception {
        String id = groupService.create(group);
        groupService.addAggregation(committee, group);

        Group fromDb = groupService.findById(id);
        assertEquals(1, fromDb.getAggregations().size());
        assertEquals(1, fromDb.getAggregations().iterator().next().getGroups().size());

        Aggregation aggregation = committeeService.findById(committee.getId());
        assertEquals(1, aggregation.getGroups().size());

        groupService.removeAggregation(committee, fromDb);

        fromDb = groupService.findById(id);
        assertEquals(0, fromDb.getAggregations().size());

        aggregation = committeeService.findById(committee.getId());
        assertEquals(0, aggregation.getGroups().size());

    }

    @Test
    public void testRemoveContactFromAggregation() throws Exception {
        groupService.create(group);
        groupService.addAggregation(committee, group);

        group = groupService.findById(group.getId());
        assertEquals(committee.getMembers().size(), group.getAggregations().iterator().next().getAggregationMembers().size());

        contactService.removeContactFromCommittee(first, committee);

        committee = committeeService.findById(committee.getId());
        group = groupService.findById(group.getId());
        assertEquals(committee.getMembers().size(), group.getAggregations().iterator().next().getAggregationMembers().size());

    }

    @Test
    @Transactional
    public void testAddEventMultipleGroups() throws Exception {

        groupService.create(group);
        groupService.addAggregation(event, group);
        groupService.addAggregation(committee, group);

        Group secondGroup = new Group();
        secondGroup.setGroupName("Second Group");
        groupService.create(secondGroup);
        groupService.addAggregation(event, secondGroup);
        groupService.addAggregation(organization, secondGroup);

        event = eventService.findById(event.getId());
        assertTrue(event.getGroups().contains(group));
        assertTrue(event.getGroups().contains(secondGroup));

        group = groupService.findById(group.getId());
        assertTrue(group.getAggregations().contains(event));

        secondGroup = groupService.findById(secondGroup.getId());
        assertTrue(secondGroup.getAggregations().contains(event));

    }

    @Test
    @Transactional
    public void testAddContactToEventMultipleGroups() throws Exception {

        groupService.create(group);
        groupService.addAggregation(event, group);

        Group secondGroup = new Group();
        secondGroup.setGroupName("Second Group");
        groupService.create(secondGroup);
        groupService.addAggregation(event, secondGroup);

        Contact newContact = new Contact();
        newContact.setFirstName("Fresh Contact");
        newContact.setEmail("Fresh email");
        contactService.create(newContact);

        contactService.attendEvent(newContact, event);

        event = eventService.findById(event.getId());
        assertTrue(event.getAttendees().contains(newContact));

        newContact = contactService.findById(newContact.getId());
        assertTrue(newContact.getAttendedEvents().contains(event));

    }

    @Test
    @Transactional
    public void testAddContactToOrganizationMultipleGroups() throws Exception {

        groupService.create(group);
        groupService.addAggregation(organization, group);

        Group secondGroup = new Group();
        secondGroup.setGroupName("Second Group");
        groupService.create(secondGroup);
        groupService.addAggregation(organization, secondGroup);

        Contact newContact = new Contact();
        newContact.setFirstName("Fresh Contact");
        newContact.setEmail("Fresh email");
        contactService.create(newContact);

        contactService.addContactToOrganization(newContact, organization);

        newContact = contactService.findById(newContact.getId());
        assertTrue(newContact.getOrganizations().contains(organization));

        group = groupService.findById(group.getId());
        assertTrue(group.getAggregations().contains(organization));

        secondGroup = groupService.findById(secondGroup.getId());
        assertTrue(secondGroup.getAggregations().contains(organization));

        organization = organizationService.findById(organization.getId());
        assertTrue(organization.getMembers().contains(newContact));


    }

}
