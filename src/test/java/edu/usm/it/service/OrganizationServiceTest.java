package edu.usm.it.service;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Contact;
import edu.usm.domain.Donation;
import edu.usm.domain.Organization;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.dto.DtoTransformer;
import edu.usm.service.ContactService;
import edu.usm.service.DonationService;
import edu.usm.service.OrganizationService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by scottkimball on 4/11/15.
 */
public class OrganizationServiceTest extends WebAppConfigurationAware {
    @Autowired
    ContactService contactService;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    DonationService donationService;

    Contact contact;
    Contact contact2;
    Organization organization;
    Donation donation;

    @Before
    public void setup() {
        organization = new Organization();
        organization.setName("organization");
        organization.setStreetAddress("123 Organizational Lane");
        organization.setCity("Portland");
        organization.setState("ME");
        organization.setZipCode("04103");
        organization.setPhoneNumber("123-456-7890");
        organization.setPrimaryContactName("Theo McCeo");
        organization.setDescription("A very good organization");

        contact = new Contact();
        contact.setFirstName("First");
        contact.setLastName("Last");
        contact.setStreetAddress("123 Fake St");
        contact.setAptNumber("# 4");
        contact.setCity("Portland");
        contact.setZipCode("04101");
        contact.setEmail("email@gmail.com");

        contact2 = new Contact();
        contact2.setFirstName("FirstName");
        contact2.setLastName("LastNAme");
        contact2.setStreetAddress("456 Fake St");
        contact2.setAptNumber("# 4");
        contact2.setCity("Lewiston");
        contact2.setZipCode("04108");
        contact2.setEmail("email@gmail.com");

        donation = new Donation();
        donation.setAmount(200);
        donation.setDateOfDeposit(LocalDate.now());
        donation.setDateOfDeposit(LocalDate.of(2015, 1, 1));
    }

    @After
    @Transactional
    public void tearDown() {
        organizationService.deleteAll();
        contactService.deleteAll();
    }

    @Test
    public void testSave () throws Exception {
        contactService.create(contact);
        contactService.create(contact2);
        organizationService.create(organization);

        contactService.addContactToOrganization(contact.getId(),organization.getId());
        contactService.addContactToOrganization(contact2.getId(), organization.getId());
        Organization orgFromDb = organizationService.findById(organization.getId());

        assertNotNull(orgFromDb);
        assertEquals(orgFromDb.getMembers().size(), 2);
        assertEquals(organization.getDescription(), orgFromDb.getDescription());
        assertEquals(organization.getName(), orgFromDb.getName());
        assertEquals(organization.getPhoneNumber(), orgFromDb.getPhoneNumber());
        assertEquals(organization.getPrimaryContactName(), orgFromDb.getPrimaryContactName());
        assertEquals(orgFromDb.getMembers().size(),2);
    }

    @Test
    public void testDelete () throws Exception {
        contactService.create(contact);
        contactService.create(contact2);
        Set<Contact> contacts = new HashSet<>();
        contacts.add(contact);
        contacts.add(contact2);
        organization.setMembers(contacts);
        String orgId = organizationService.create(organization);

        contactService.addContactToOrganization(contact.getId(), orgId);
        contactService.addContactToOrganization(contact2.getId(), orgId);

        Contact contactFromDb = contactService.findById(contact.getId());
        Set<Organization> organizations = contactService.getAllContactOrganizations(contactFromDb.getId());
        assertEquals(organizations.size(),1); // before
        organizationService.deleteAll();

        contactFromDb = contactService.findById(contact.getId()); // after
        organizations = contactService.getAllContactOrganizations(contactFromDb.getId());
        assertNotNull(contactFromDb);
        assertEquals(organizations.size(),0);
    }

    @Test(expected = ConstraintViolation.class)
    public void testCreateNullName() throws ConstraintViolation, NullDomainReference {
        organization.setName(null);
        organizationService.create(organization);
    }

    @Test(expected = ConstraintViolation.class)
    public void testUpdateNullName() throws ConstraintViolation, NullDomainReference {
        organizationService.create(organization);
        organization.setName(null);
        organizationService.updateOrganizationDetails(organization.getId(),organization);
    }

    @Test
    public void testAddDonation() throws Exception{
        organizationService.create(organization);
        organizationService.addDonation(organization.getId(), DtoTransformer.fromEntity(donation));

        organization = organizationService.findById(organization.getId());
        String donationId = organizationService.getDonations(organization.getId()).iterator().next().getId();
        assertFalse(organizationService.getDonations(organization.getId()).isEmpty());

        donation = donationService.findById(donationId);
        assertNotNull(donation);
    }

    @Test
    public void testRemoveDonation() throws Exception {
        organization.addDonation(donation);
        organizationService.create(organization);

        organization = organizationService.findById(organization.getId());
        donation = organizationService.getDonations(organization.getId()).iterator().next();
        assertNotNull(donation);
        organizationService.removeDonation(organization.getId(), donation.getId());

        donation = donationService.findById(donation.getId());
        assertNotNull(donation);

        organization = organizationService.findById(organization.getId());
        assertTrue(organizationService.getDonations(organization.getId()).isEmpty());
    }

    @Test
    public void findOrganizationWithDonation() throws Exception {
        organizationService.create(organization);
        organizationService.addDonation(organization.getId(), DtoTransformer.fromEntity(donation));

        organization = organizationService.findById(organization.getId());
        donation = organizationService.getDonations(organization.getId()).iterator().next();

        Organization withDonation = organizationService.findOrganizationWithDonation(donation);
        assertEquals(organization, withDonation);
    }
}
