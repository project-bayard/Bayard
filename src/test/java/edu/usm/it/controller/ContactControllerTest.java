package edu.usm.it.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usm.config.DateFormatConfig;
import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.*;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.dto.DtoTransformer;
import edu.usm.dto.EncounterDto;
import edu.usm.dto.IdDto;
import edu.usm.service.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Iterator;
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
    EncounterService encounterService;

    @Autowired
    DateFormatConfig dateFormatConfig;

    @Autowired
    EncounterTypeService encounterTypeService;

    @Autowired
    GroupService groupService;

    @Autowired
    DonationService donationService;


    private Contact contact;
    private Event event;
    private EncounterType encounterType;
    private Group group;
    private Donation donation;
    private SustainerPeriod sustainerPeriod;

    @Before
    public void setup() throws ConstraintViolation{
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

        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setPaidDues(true);
        memberInfo.setSignedAgreement(false);
        memberInfo.setStatus(MemberInfo.STATUS_GOOD);
        contact.setMemberInfo(memberInfo);

        event = new Event();
        event.setName("Test Event");
        event.setLocation("Test event location");
        event.setDateHeld(dateFormatConfig.formatDomainDate(LocalDate.of(2015, 01, 01)));
        event.setNotes("Test event notes");

        encounterType = new EncounterType();
        encounterType.setName("Name");
        encounterTypeService.create(encounterType);

        group = new Group();
        group.setGroupName("Test Group");

        donation = new Donation();
        donation.setAmount(200);
        donation.setDateOfDeposit(LocalDate.now());
        donation.setDateOfDeposit(LocalDate.of(2015, 1, 1));

        sustainerPeriod = new SustainerPeriod();
        sustainerPeriod.setPeriodStartDate(LocalDate.of(2015, 1, 1));
        sustainerPeriod.setCancelDate(LocalDate.now());
        sustainerPeriod.setSentIRSLetter(true);
        sustainerPeriod.setMonthlyAmount(20);
    }

    @After
    public void teardown () {

        groupService.deleteAll();
        organizationService.deleteAll();
        committeeService.deleteAll();
        contactService.deleteAll();
        eventService.deleteAll();
        encounterTypeService.deleteAll();
        donationService.deleteAll();
    }

    @Test
    @Transactional
    public void testPostContact() throws Exception {
        String json = new ObjectMapper().writeValueAsString(contact);

        mockMvc.perform(post("/contacts").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        Set<Contact> fromDb = contactService.findAll();
        assertEquals(fromDb.size(), 1);

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

        Contact details = new Contact();
        details.setFirstName("newFirstName");
        details.setEmail("email@email.com");

        String json = new ObjectMapper().writeValueAsString(details);

        mockMvc.perform(put("/contacts/" + contact.getId())
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        Contact fromDb = contactService.findById(contact.getId());
        assertEquals(fromDb.getFirstName(), details.getFirstName());

        //TODO: Test all fields

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
        contact.setEmail("email@email.com");
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
        assertEquals(fromDb.getOrganizations().iterator().next().getId(), organization.getId());
        assertNotNull(orgFromDb);
        assertEquals(orgFromDb.getMembers().iterator().next().getId(), contact.getId());


        /*Bad Contact Id*/
        mockMvc.perform(put("/contacts/" + "badId" + "/organizations")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));


        /*Bad org ID*/
        organizationIdDto.setId("badId");
        json = mapper.writeValueAsString(organizationIdDto);
        mockMvc.perform(put(path)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
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

        mockMvc.perform(delete("/contacts/" + id + "/organizations/" + orgID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testCreateEncounter() throws Exception {
        String id = contactService.create(contact);
        String initiatorId = contactService.create(generateSecondcontact("Initiator", "initiatorEmail"));
        EncounterDto dto = new EncounterDto();
        dto.setInitiatorId(initiatorId);
        dto.setNotes("Notes");
        dto.setAssessment(9);
        dto.setType(encounterType.getId());
        dto.setEncounterDate(dateFormatConfig.formatDomainDate(LocalDate.now()));

        String json = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(put("/contacts/" + id + "/encounters")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteEncounter() throws Exception {
        String id = contactService.create(contact);
        String initiatorId = contactService.create(generateSecondcontact("Initiator", "initiatorEmail"));
        EncounterDto dto = new EncounterDto();
        dto.setInitiatorId(initiatorId);
        dto.setNotes("Notes");
        dto.setAssessment(9);
        dto.setType(encounterType.getId());
        dto.setEncounterDate(dateFormatConfig.formatDomainDate(LocalDate.now()));

        String json = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(put("/contacts/" + id + "/encounters")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        Contact contactFromDb = contactService.findById(id);
        Encounter encounter = contactFromDb.getEncounters().first();

        mockMvc.perform(delete("/contacts/" + id + "/encounters/" + encounter.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        contactFromDb = contactService.findById(id);
        assertEquals(0, contactFromDb.getEncounters().size());
    }

    @Test
    @Transactional
    public void testUpdateEncounter() throws Exception {
        String id = contactService.create(contact);
        String initiatorId = contactService.create(generateSecondcontact("Second", "secondEmail"));
        EncounterDto dto = new EncounterDto();
        dto.setInitiatorId(initiatorId);
        dto.setNotes("Notes");
        dto.setAssessment(9);
        dto.setType(encounterType.getId());
        dto.setEncounterDate(dateFormatConfig.formatDomainDate(LocalDate.now()));

        String json = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(put("/contacts/" + id + "/encounters")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        Contact contactFromDb = contactService.findById(id);
        Encounter encounterFromDb = contactFromDb.getEncounters().first();
        String newInitiatorId = contactService.create(generateSecondcontact("Third", "thirdEmail"));

        dto = new EncounterDto();
        dto.setAssessment(5);
        dto.setNotes("Updated Notes");
        dto.setType(encounterType.getId());
        dto.setInitiatorId(newInitiatorId);
        dto.setEncounterDate(dateFormatConfig.formatDomainDate(LocalDate.now()));

        json = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(put("/contacts/" + id + "/encounters/" + encounterFromDb.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        encounterFromDb = encounterService.findById(encounterFromDb.getId());
        assertEquals("Updated Notes", encounterFromDb.getNotes());
        assertEquals(newInitiatorId, encounterFromDb.getInitiator().getId());

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
                .andExpect(status().isOk());}


    @Test
    public void testRemoveContactFromCommittee () throws Exception {
        String id = contactService.create(contact);

        Committee committee = new Committee();
        committee.setName("orgName");
        HashSet members = new HashSet();
        members.add(contact);
        committee.setMembers(members);

        committeeService.create(committee);

        String committeeID = committeeService.create(committee);

        mockMvc.perform(delete("/contacts/" + id + "/committees/" + committeeID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
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

    @Test
    public void testGetMemberInfo() throws Exception {
        contactService.create(contact);

        mockMvc.perform(get("/contacts/"+contact.getId()+"/memberinfo").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(contact.getMemberInfo().getStatus())))
                .andExpect(jsonPath("$.paidDues", is(contact.getMemberInfo().hasPaidDues())))
                .andExpect(jsonPath("$.signedAgreement", is(contact.getMemberInfo().hasSignedAgreement())));

    }

    @Test
    public void testUpdateMemberInfo() throws Exception {
        contactService.create(contact);

        MemberInfo newInfo = new MemberInfo();
        newInfo.setStatus(MemberInfo.STATUS_BAD);
        newInfo.setSignedAgreement(true);
        newInfo.setPaidDues(false);

        String json = new ObjectMapper().writeValueAsString(newInfo);

        mockMvc.perform(put("/contacts/" + contact.getId() + "/memberinfo")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveContactFromEvent() throws Exception {
        contactService.create(contact);
        eventService.create(event);
        contact.setAttendedEvents(new HashSet<>());
        contactService.attendEvent(contact, event);

        mockMvc.perform(delete("/contacts/"+contact.getId()+"/events/"+event.getId())
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Contact contactFromDb = contactService.findById(contact.getId());
        assertFalse(contactFromDb.getAttendedEvents().contains(event));

        Event eventFromDb = eventService.findById(event.getId());
        assertFalse(eventFromDb.getAttendees().contains(contact));

    }

    private Contact generateSecondcontact(String firstName, String email) {
        Contact secondContact = new Contact();
        secondContact.setFirstName(firstName);
        secondContact.setLastName("ToLast");
        secondContact.setStreetAddress("541 Downtown Abbey");
        secondContact.setAptNumber("# 9");
        secondContact.setCity("Yarmouth");
        secondContact.setZipCode("04096");
        secondContact.setEmail(email);
        secondContact.setInitiator(true);
        return secondContact;
    }

    @Test
    public void testAddContactToGroup () throws  Exception {
        String id = contactService.create(contact);
        String groupId = groupService.create(group);
        eventService.create(event);
        contactService.attendEvent(contact, event);

        group = groupService.findById(groupId);
        groupService.addAggregation(event, group);

        IdDto idDto = new IdDto(groupId);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(idDto);
        String path = "/contacts/" + id + "/groups";

        mockMvc.perform(put(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        group = groupService.findById(groupId);
        assertEquals(1, group.getTopLevelMembers().size());

        contact = contactService.findById(id);
        assertEquals(1, contact.getGroups().size());

    }

    @Test
    public void testRemoveContactFromGroup() throws Exception {
        String id = contactService.create(contact);
        String groupId = groupService.create(group);
        eventService.create(event);
        contactService.attendEvent(contact, event);
        group = groupService.findById(groupId);
        groupService.addAggregation(event, group);
        group = groupService.findById(groupId);
        contact = contactService.findById(contact.getId());
        contactService.addToGroup(contact, group);

        mockMvc.perform(delete("/contacts/" + id + "/groups/" + groupId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        group = groupService.findById(groupId);
        assertEquals(0, group.getTopLevelMembers().size());

        contact = contactService.findById(id);
        assertEquals(0, contact.getGroups().size());
    }
    @Test
    public void testGetAllContactGroups () throws Exception {
        String id = contactService.create(contact);
        String groupId = groupService.create(group);
        eventService.create(event);
        contactService.attendEvent(contact, event);
        group = groupService.findById(groupId);
        groupService.addAggregation(event, group);
        group = groupService.findById(groupId);
        contact = contactService.findById(contact.getId());
        contactService.addToGroup(contact, group);

        mockMvc.perform(get("/contacts/" + contact.getId() + "/groups")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("[0].id", is(groupId)))
                .andExpect(jsonPath("[0].groupName", is(group.getGroupName())));
    }

    @Test
    public void testAddContactToEventMultipleGroups() throws Exception {

        contactService.create(contact);
        eventService.create(event);

        groupService.create(group);
        Group secondGroup = new Group();
        secondGroup.setGroupName("Second Group");
        groupService.create(group);

        groupService.addAggregation(event, group);
        groupService.addAggregation(event, secondGroup);

        IdDto eventIdDto = new IdDto(event.getId());
        String requestBody = new ObjectMapper().writeValueAsString(eventIdDto);
        String path = "/contacts/"+contact.getId()+"/events";

        mockMvc.perform(put(path)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());

        event = eventService.findById(event.getId());
        assertTrue(event.getGroups().contains(group));
        assertTrue(event.getGroups().contains(secondGroup));

        contact = contactService.findById(contact.getId());
        assertTrue(contact.getAttendedEvents().contains(event));

    }

    @Test
    public void testAddDonation() throws Exception {
        contactService.create(contact);
        BayardTestUtilities.performEntityPost("/contacts/" + contact.getId() + "/donations", donation, mockMvc);

        contact = contactService.findById(contact.getId());
        assertFalse(contact.getDonorInfo().getDonations().isEmpty());
    }

    @Test
    public void testRemoveDonation() throws Exception {
        contactService.create(contact);
        contactService.addDonation(contact, donation);
        contact = contactService.findById(contact.getId());
        donation = contact.getDonorInfo().getDonations().iterator().next();
        assertNotNull(donation);

        String url = "/contacts/" + contact.getId() + "/donations/"+donation.getId();
        BayardTestUtilities.performEntityDelete(url, mockMvc);

        contact = contactService.findById(contact.getId());
        assertTrue(contact.getDonorInfo().getDonations().isEmpty());
        donation = donationService.findById(donation.getId());
        assertNotNull(donation);
    }

    @Test
    public void testGetSustainerPeriod() throws Exception {
        contactService.create(contact);
        contactService.createSustainerPeriod(contact, sustainerPeriod);

        contact = contactService.findById(contact.getId());
        sustainerPeriod = contact.getDonorInfo().getSustainerPeriods().iterator().next();

        String url = "/contacts/"+contact.getId()+"/sustainer/"+sustainerPeriod.getId();
        BayardTestUtilities.performEntityGetSingle(Views.SustainerPeriodDetails.class, url, mockMvc, sustainerPeriod);
    }

    @Test
    public void testGetAllSustainerPeriods() throws Exception {
        contactService.create(contact);
        contactService.createSustainerPeriod(contact, sustainerPeriod);
        SustainerPeriod secondPeriod = new SustainerPeriod();
        secondPeriod.setMonthlyAmount(1000);
        secondPeriod.setCancelDate(LocalDate.now());
        secondPeriod.setPeriodStartDate(LocalDate.of(2014, 2, 2));
        secondPeriod.setSentIRSLetter(true);
        contactService.createSustainerPeriod(contact, secondPeriod);

        contact = contactService.findById(contact.getId());
        Iterator<SustainerPeriod> it = contact.getDonorInfo().getSustainerPeriods().iterator();
        String url = "/contacts/"+contact.getId()+"/sustainer";
        BayardTestUtilities.performEntityGetMultiple(Views.SustainerPeriodDetails.class, url, mockMvc, it.next(), it.next());
    }
    @Test
    public void testCreateSustainerPeriod() throws Exception {
        contactService.create(contact);

        String url = "/contacts/"+contact.getId()+"/sustainer";
        BayardTestUtilities.performEntityPost(url, DtoTransformer.fromEntity(sustainerPeriod), mockMvc);

        contact = contactService.findById(contact.getId());
        assertFalse(contact.getDonorInfo().getSustainerPeriods().isEmpty());

    }
    @Test
    public void testUpdateSustainerPeriod() throws Exception {
        contactService.create(contact);
        contactService.createSustainerPeriod(contact, sustainerPeriod);

        contact = contactService.findById(contact.getId());
        sustainerPeriod = contact.getDonorInfo().getSustainerPeriods().iterator().next();
        int newMonthlyAmount = sustainerPeriod.getMonthlyAmount() + 200;
        sustainerPeriod.setMonthlyAmount(newMonthlyAmount);
        String url = "/contacts/"+contact.getId()+"/sustainer/"+sustainerPeriod.getId();
        BayardTestUtilities.performEntityPut(url, DtoTransformer.fromEntity(sustainerPeriod), mockMvc);

        contact = contactService.findById(contact.getId());
        sustainerPeriod = contact.getDonorInfo().getSustainerPeriods().iterator().next();
        assertEquals(newMonthlyAmount, sustainerPeriod.getMonthlyAmount());
    }
    @Test
    public void testDeleteSustainerPeriod() throws Exception {
        contactService.create(contact);
        contactService.createSustainerPeriod(contact, sustainerPeriod);
        contact = contactService.findById(contact.getId());
        sustainerPeriod = contact.getDonorInfo().getSustainerPeriods().iterator().next();

        String url = "/contacts/"+contact.getId()+"/sustainer/"+sustainerPeriod.getId();
        BayardTestUtilities.performEntityDelete(url, mockMvc);
        contact = contactService.findById(contact.getId());
        assertTrue(contact.getDonorInfo().getSustainerPeriods().isEmpty());
    }


}
