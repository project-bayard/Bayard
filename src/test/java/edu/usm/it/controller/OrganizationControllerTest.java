package edu.usm.it.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.BudgetItem;
import edu.usm.domain.Contact;
import edu.usm.domain.Donation;
import edu.usm.domain.Organization;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.dto.DtoTransformer;
import edu.usm.service.ContactService;
import edu.usm.service.DonationService;
import edu.usm.service.OrganizationService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by scottkimball on 5/28/15.
 */
public class OrganizationControllerTest extends WebAppConfigurationAware {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ContactService contactService;

    @Autowired
    DonationService donationService;

    final static String ORGANIZATIONS_BASE_URL = "/organizations/";

    private Organization organization;
    private Contact contact;
    private Contact initiator;
    private BudgetItem budgetItem;
    private Donation donation;

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

        budgetItem = new BudgetItem("Test Budget Item");
        donationService.createBudgetItem(budgetItem);

        donation = new Donation();
        donation.setAmount(300);
        donation.setDateOfDeposit(LocalDate.now());
        donation.setDateOfReceipt(LocalDate.of(2015, 1, 1));
        donation.setMethod("Credit Card");
        donation.setBudgetItem(budgetItem);
    }

    @After
    public void teardown() {
        contactService.deleteAll();
        organizationService.deleteAll();
        donationService.deleteAllBudgetItems();
        donationService.deleteAll();
    }


    @Test
    public void testGetAllOrganizations() throws Exception {

        organization.getMembers().add(contact);
        Set<Organization> organizations = new HashSet<>();
        organizations.add(organization);
        contact.setOrganizations(organizations);
        contactService.create(contact);


        MvcResult result = mockMvc.perform(get("/organizations").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(contact.getOrganizations().iterator().next().getId())))
                .andExpect(jsonPath("$.[0].name", is(organization.getName())))
                .andExpect(jsonPath("$.[0].streetAddress", is(organization.getStreetAddress())))
                .andExpect(jsonPath("$.[0].city", is(organization.getCity())))
                .andExpect(jsonPath("$.[0].state", is(organization.getState())))
                .andExpect(jsonPath("$.[0].zipCode", is(organization.getZipCode())))
                .andExpect(jsonPath("$.[0].phoneNumber", is(organization.getPhoneNumber())))
                .andExpect(jsonPath("$.[0].primaryContactName", is(organization.getPrimaryContactName())))
                .andExpect(jsonPath("$.[0].description", is(organization.getDescription())))
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

    @Test(expected = NullDomainReference.NullOrganization.class)
    public void testDeleteOrganization() throws Exception {

        organization.getMembers().add(contact);
        Set<Organization> organizations = new HashSet<>();
        organizations.add(organization);
        contact.setOrganizations(organizations);
        contactService.create(contact);

        String organizationId = organization.getId();
        assertNotNull(organizationService.findById(organizationId));

        mockMvc.perform(delete("/organizations/"+organizationId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Contact persistedContact = contactService.findById(contact.getId());
        Set<Organization> orgsFromPersistedContact = contactService.getAllContactOrganizations(persistedContact.getId());
        assertEquals(0, orgsFromPersistedContact.size());

        organizationService.findById(organizationId); // Should throw exception here
    }

    @Test
    public void testUpdatePrimitives() throws Exception {

        organizationService.create(organization);

        organization.setName("Updated Name");
        organization.setPrimaryContactName("Updated Primary Contact");

        String json = new ObjectMapper().writeValueAsString(organization);

        mockMvc.perform(put("/organizations/" + organization.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Organization fromDb = organizationService.findById(organization.getId());
        assertEquals("Updated Name", fromDb.getName());
        assertEquals("Updated Primary Contact", fromDb.getPrimaryContactName());

    }

    @Test
    public void testUpdateTruncatedObjectGraph() throws Exception {

        organization.getMembers().add(contact);
        Set<Organization> organizations = new HashSet<>();
        organizations.add(organization);
        contact.setOrganizations(organizations);
        contactService.create(contact);

        organization.setMembers(null);
        organization.setName("Updated Name");

        String json = new ObjectMapper().writeValueAsString(organization);

        MvcResult result = mockMvc.perform(put("/organizations/" + organization.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Organization fromDb = organizationService.findById(organization.getId());
        assertTrue(fromDb.getMembers().contains(contact));
        assertEquals("Updated Name", fromDb.getName());

    }

    @Test
    public void testAddDonation() throws Exception {
        organizationService.create(organization);
        String url = ORGANIZATIONS_BASE_URL + organization.getId() + "/donations";
        BayardTestUtilities.performEntityPost(url, DtoTransformer.fromEntity(donation), mockMvc);

        organization = organizationService.findById(organization.getId());
        assertFalse(organizationService.getDonations(organization.getId()).isEmpty());
        assertEquals(budgetItem.getName(), organizationService.getDonations(organization.getId()).iterator().next().getBudgetItemName());
    }

    @Test
    public void testRemoveDonation() throws Exception {
        organizationService.create(organization);
        organizationService.addDonation(organization.getId(), DtoTransformer.fromEntity(donation));
        organization = organizationService.findById(organization.getId());
        donation = organizationService.getDonations(organization.getId()).iterator().next();

        String url = ORGANIZATIONS_BASE_URL + organization.getId() + "/donations/"+donation.getId();
        BayardTestUtilities.performEntityDelete(url, mockMvc);
        organization = organizationService.findById(organization.getId());
        assertTrue(organizationService.getDonations(organization.getId()).isEmpty());
        donation = donationService.findById(donation.getId());
        assertNotNull(donation);

    }


}
