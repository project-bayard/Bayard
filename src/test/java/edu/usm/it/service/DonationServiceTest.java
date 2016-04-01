package edu.usm.it.service;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Contact;
import edu.usm.domain.Donation;
import edu.usm.domain.Organization;
import edu.usm.service.ContactService;
import edu.usm.service.DonationService;
import edu.usm.service.OrganizationService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * Created by andrew on 2/25/16.
 */
public class DonationServiceTest extends WebAppConfigurationAware {

    @Autowired
    DonationService donationService;

    @Autowired
    ContactService contactService;

    @Autowired
    OrganizationService organizationService;

    Donation donation;
    Organization organization;
    Contact contact;

    @Before
    public void setup() {
        donation = new Donation();
        donation.setAmount(20);
        donation.setAnonymous(true);
        donation.setBudgetItem("Misc");
        donation.setDateOfDeposit(LocalDate.of(2015, 12, 12));
        donation.setDateOfReceipt(LocalDate.of(2015, 11, 11));
        donation.setMethod("Credit Card");
        donation.setRestrictedToCategory("Office supplies");
        donation.setStandalone(true);

        contact = new Contact();
        contact.setFirstName("Test");
        contact.setEmail("test@email.com");

        organization = new Organization();
        organization.setName("Test Organization");
    }

    @Autowired
    public void tearDown() {
        contactService.deleteAll();
        organizationService.deleteAll();
        donationService.deleteAll();
    }

    @Test
    public void testCreateDonation() {
        donationService.create(donation);
        donation = donationService.findById(donation.getId());
        assertNotNull(donation);
    }

    @Test
    public void testUpdateDonation() {
        donationService.create(donation);
        int newDonationAmount = donation.getAmount() + 10;
        donation.setAmount(newDonationAmount);
        donationService.update(donation);

        donation = donationService.findById(donation.getId());
        assertEquals(newDonationAmount, donation.getAmount());
    }

    @Test
    public void testDeleteDonation() {
        donationService.create(donation);
        donation = donationService.findById(donation.getId());

        donationService.delete(donation);
        donation = donationService.findById(donation.getId());

        assertNull(donation);
    }

    @Test
    public void testCreateAndDeleteDonationMultipleSources() throws Exception {
        contactService.create(contact);
        organizationService.create(organization);

        contactService.addDonation(contact, donation);
        contact = contactService.findById(contact.getId());
        donation = contact.getDonorInfo().getDonations().iterator().next();
        organizationService.addDonation(organization.getId(), donation);

        contact = contactService.findById(contact.getId());
        donation = donationService.findById(donation.getId());
        organization = organizationService.findById(organization.getId());
        assertTrue(contact.getDonorInfo().getDonations().contains(donation));
        assertTrue(organization.getDonations().contains(donation));

        contactService.removeDonation(contact, donation);
        contact = contactService.findById(contact.getId());
        organization = organizationService.findById(organization.getId());
        assertFalse(contact.getDonorInfo().getDonations().contains(donation));
        assertTrue(organization.getDonations().contains(donation));

        organizationService.removeDonation(organization.getId(), donation.getId());
        organization = organizationService.findById(organization.getId());
        donation = donationService.findById(donation.getId());
        assertNotNull(donation);
        assertFalse(organization.getDonations().contains(donation));
    }


}
