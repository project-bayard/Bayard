package edu.usm.it.service;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Contact;
import edu.usm.domain.Donation;
import edu.usm.domain.Organization;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.service.ContactService;
import edu.usm.service.DonationService;
import edu.usm.service.OrganizationService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
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
    public void tearDown() {
        organizationService.deleteAll();
        contactService.deleteAll();
    }

    @Test
    @Transactional
    public void testSave () throws Exception {

        contactService.create(contact);
        contactService.create(contact2);
        organizationService.create(organization);

        contactService.addContactToOrganization(contact,organization);
        contactService.addContactToOrganization(contact2, organization);
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
    @Transactional
    public void testDelete () throws Exception {
        Set<Contact> contacts = new HashSet<>();
        contacts.add(contact);
        contacts.add(contact2);

        organization.setMembers(contacts);
        organizationService.create(organization);

        contactService.addContactToOrganization(contact, organization);
        contactService.addContactToOrganization(contact2, organization);

        Contact contactFromDb = contactService.findById(contact.getId());
        assertEquals(contactFromDb.getOrganizations().size(),1); // before
        organizationService.deleteAll();

        contactFromDb = contactService.findById(contact.getId()); // after
        assertNotNull(contactFromDb);
        assertEquals(contactFromDb.getOrganizations().size(),0);

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
        organizationService.update(organization);
    }

    @Test
    public void testAddDonation() throws Exception{
        organizationService.create(organization);
        organizationService.addDonation(organization, donation);

        organization = organizationService.findById(organization.getId());
        String donationId = organization.getDonations().iterator().next().getId();
        assertFalse(organization.getDonations().isEmpty());

        donation = donationService.findById(donationId);
        assertNotNull(donation);
    }

    @Test
    public void testRemoveDonation() throws Exception {
        organization.addDonation(donation);
        organizationService.create(organization);

        organization = organizationService.findById(organization.getId());
        donation = organization.getDonations().iterator().next();
        assertNotNull(donation);
        organizationService.removeDonation(organization, donation);

        donation = donationService.findById(donation.getId());
        assertNotNull(donation);

        organization = organizationService.findById(organization.getId());
        assertTrue(organization.getDonations().isEmpty());
    }
}
