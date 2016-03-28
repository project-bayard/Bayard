package edu.usm.it.service;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.BudgetItem;
import edu.usm.domain.Contact;
import edu.usm.domain.Donation;
import edu.usm.domain.Organization;
import edu.usm.repository.BudgetItemDao;
import edu.usm.service.ContactService;
import edu.usm.service.DonationService;
import edu.usm.service.OrganizationService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Set;

import static junit.framework.TestCase.assertNotNull;
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

    @Autowired
    BudgetItemDao budgetItemDao;

    Donation donation;
    Organization organization;
    Contact contact;
    BudgetItem budgetItem;


    @Before
    public void setup() {

        budgetItem = new BudgetItem("Misc");
        budgetItemDao.save(budgetItem);

        donation = new Donation();
        donation.setAmount(20);
        donation.setAnonymous(true);
        donation.setBudgetItem(budgetItem);
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
        donationService.deleteAllBudgetItems();
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
        organizationService.addDonation(organization, donation);

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

        organizationService.removeDonation(organization, donation);
        organization = organizationService.findById(organization.getId());
        donation = donationService.findById(donation.getId());
        assertNotNull(donation);
        assertFalse(organization.getDonations().contains(donation));
    }

    @Test
    public void testCreateBudgetItem() {
        BudgetItem budgetItem = new BudgetItem("Test Budget Item");
        donationService.createBudgetItem(budgetItem);
        budgetItem = donationService.findBudgetItem(budgetItem.getId());
        assertNotNull(budgetItem);
    }

    @Test
    public void testDeleteBudgetItem() {
        BudgetItem budgetItem = new BudgetItem("Test Budget Item");
        donationService.createBudgetItem(budgetItem);
        budgetItem = donationService.findBudgetItem(budgetItem.getId());
        donation.setBudgetItem(budgetItem);
        donationService.create(donation);

        donation = donationService.findById(donation.getId());
        assertNotNull(donation);

        donationService.deleteBudgetItem(budgetItem);
        budgetItem = budgetItemDao.findOne(budgetItem.getId());
        assertNull(budgetItem);

        donation = donationService.findById(donation.getId());
        assertNotNull(donation);
    }

    @Test
    public void testDeleteAllBudgetItems() {
        BudgetItem secondBudgetItem = new BudgetItem("Second Budget Item");
        donationService.createBudgetItem(budgetItem);
        donationService.createBudgetItem(secondBudgetItem);

        donation.setBudgetItem(budgetItem);
        Donation secondDonation = new Donation(100, "Credit Card", LocalDate.now(), LocalDate.now());
        secondDonation.setBudgetItem(secondBudgetItem);
        Donation thirdDonation = new Donation(150, "Credit Card", LocalDate.now(), LocalDate.now());
        thirdDonation.setBudgetItem(secondBudgetItem);

        donationService.create(donation);
        donationService.create(secondDonation);
        donationService.create(thirdDonation);

        Set<Donation> firstBudgetItemDonations = donationService.findByBudgetItem(budgetItem);
        assertEquals(1, firstBudgetItemDonations.size());
        Set<Donation> secondBudgetItemDonations = donationService.findByBudgetItem(secondBudgetItem);
        assertEquals(2, secondBudgetItemDonations.size());

        donationService.deleteAllBudgetItems();

        firstBudgetItemDonations = donationService.findByBudgetItem(budgetItem);
        assertTrue(firstBudgetItemDonations.isEmpty());
        secondBudgetItemDonations = donationService.findByBudgetItem(secondBudgetItem);
        assertTrue(secondBudgetItemDonations.isEmpty());
    }

    @Test
    public void testUpdateBudgetItemName() throws Exception {
        donationService.createBudgetItem(budgetItem);
        donation.setBudgetItem(budgetItem);
        donationService.create(donation);

        donation = donationService.findById(donation.getId());
        assertEquals(budgetItem.getName(), donation.getBudgetItem().getName());

        String newBudgetItemName = "Updated name";
        donationService.updateBudgetItemName(budgetItem, newBudgetItemName);

        donation = donationService.findById(donation.getId());
        assertEquals(budgetItem.getName(), newBudgetItemName);

    }

    @Test
    public void testGetDonationsByBudgetItemPageable() throws Exception {
        donationService.createBudgetItem(budgetItem);
        donation.setBudgetItem(budgetItem);
        donationService.create(donation);

        Pageable pageable = new PageRequest(0, 5);
        Page<Donation> donations = donationService.findDonationsByBudgetItem(budgetItem.getId(), pageable);
        assertTrue(donations.getTotalElements() == 1);
        assertTrue(donations.getContent().contains(donation));
    }
}
