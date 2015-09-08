package edu.usm.it.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usm.config.DateFormatConfig;
import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.*;
import edu.usm.domain.exception.ConstraintMessage;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.dto.EncounterDto;
import edu.usm.service.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.HashSet;

import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by andrew on 8/28/15.
 */
public class ControllerExceptionHandlerTest extends WebAppConfigurationAware {

    @Autowired
    DateFormatConfig dateFormatConfig;

    @Autowired
    ContactService contactService;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    CommitteeService committeeService;

    @Autowired
    EventService eventService;

    @Autowired
    EncounterService encounterService;

    @Autowired
    EncounterTypeService encounterTypeService;

    private Contact contact;
    private Event event;
    private Organization organization;
    private EncounterType encounterType;
    private Committee committee;


    @Before
    public void setup() throws ConstraintViolation {
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


        encounterType = new EncounterType();
        encounterType.setName("Name");
        encounterTypeService.create(encounterType);

        committee = new Committee();
        committee.setName("New Committee");

    }

    @After
    public void teardown () {

        encounterTypeService.deleteAll();
        organizationService.deleteAll();
        contactService.deleteAll();
        eventService.deleteAll();
    }

    @Test
    public void testPostNonUniqueContact() throws Exception{

        contactService.create(contact);
        Contact duplicate = new Contact();
        duplicate.setFirstName(contact.getFirstName());
        duplicate.setEmail(contact.getEmail());
        String json = new ObjectMapper().writeValueAsString(duplicate);

        mockMvc.perform(post("/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", startsWith(ConstraintMessage.CONTACT_DUPLICATE_NAME_EMAIL.toString())));

    }

    @Test
    public void testPostContactNoName() throws Exception{

        contact.setFirstName(null);
        String json = new ObjectMapper().writeValueAsString(contact);

        mockMvc.perform(post("/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", startsWith(ConstraintMessage.CONTACT_NO_FIRST_NAME.toString())));

    }

    @Test
    public void testPostContactNoPhoneNoEmail() throws Exception{

        contact.setPhoneNumber1(null);
        contact.setEmail(null);
        String json = new ObjectMapper().writeValueAsString(contact);

        mockMvc.perform(post("/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", startsWith(ConstraintMessage.CONTACT_NO_EMAIL_OR_PHONE_NUMBER.toString())));

    }

    @Test
    public void testPostNonUniqueEvent() throws Exception {
        eventService.create(event);

        Event duplicate = new Event();
        duplicate.setName(event.getName());
        duplicate.setDateHeld(event.getDateHeld());
        String json = new ObjectMapper().writeValueAsString(duplicate);

        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", startsWith(ConstraintMessage.EVENT_NON_UNIQUE.toString())));

    }

    @Test
    public void testPostEventNoName() throws Exception {
        event.setName(null);
        String json = new ObjectMapper().writeValueAsString(event);

        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", startsWith(ConstraintMessage.EVENT_REQUIRED_NAME.toString())));

    }

    @Test
    public void testPostEventNoDate() throws Exception {
        event.setDateHeld(null);
        String json = new ObjectMapper().writeValueAsString(event);

        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", startsWith(ConstraintMessage.EVENT_REQUIRED_DATE.toString())));

    }

    @Test
    public void testPostOrganizationNoName() throws Exception {

        organization.setName(null);
        String json = new ObjectMapper().writeValueAsString(organization);
        mockMvc.perform(post("/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", startsWith(ConstraintMessage.ORGANIZATION_REQUIRED_NAME.toString())));

    }

    @Test
    public void testUpdateOrganizationNoName() throws Exception {

        organization.setName(null);
        String json = new ObjectMapper().writeValueAsString(organization);
        mockMvc.perform(post("/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", startsWith(ConstraintMessage.ORGANIZATION_REQUIRED_NAME.toString())));

    }

    @Test
    public void testCreateEncounterNoEncounterType() throws Exception {
        String id = contactService.create(contact);
        String initiatorId = contactService.create(generateSecondcontact("Initiator", "initiatorEmail"));
        EncounterDto dto = new EncounterDto();
        dto.setInitiatorId(initiatorId);
        dto.setNotes("Notes");
        dto.setAssessment(9);
        dto.setType(null);
        dto.setEncounterDate(dateFormatConfig.formatDomainDate(LocalDate.now()));

        String json = new ObjectMapper().writeValueAsString(dto);

        String errorMessage = new NullDomainReference.NullEncounterType().getMessage();

        mockMvc.perform(put("/contacts/" + id + "/encounters")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", startsWith(errorMessage)));
    }

    @Test
    public void testCreateEncounterNoEncounterDate() throws Exception {

        String id = contactService.create(contact);
        String initiatorId = contactService.create(generateSecondcontact("Initiator", "initiatorEmail"));
        EncounterDto dto = new EncounterDto();
        dto.setInitiatorId(initiatorId);
        dto.setNotes("Notes");
        dto.setAssessment(9);
        dto.setType(encounterType.getId());
        dto.setEncounterDate(null);

        String json = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(put("/contacts/" + id + "/encounters")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", startsWith(ConstraintMessage.ENCOUNTER_REQUIRED_DATE.toString())));

    }

    @Test
    public void testCreateCommitteeNoName() throws Exception {

        committee.setName(null);
        String json = new ObjectMapper().writeValueAsString(committee);

        mockMvc.perform(post("/committees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", startsWith(ConstraintMessage.COMMITTEE_REQUIRED_NAME.toString())));

    }

    @Test
    public void testCreateCommitteeNonUnique() throws Exception {

        committeeService.create(committee);

        Committee duplicate = new Committee();
        duplicate.setName(committee.getName());

        String json = new ObjectMapper().writeValueAsString(duplicate);

        mockMvc.perform(post("/committees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", startsWith(ConstraintMessage.COMMITTEE_NON_UNIQUE.toString())));

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


}
