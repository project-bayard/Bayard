package edu.usm.it.dao;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.*;
import edu.usm.repository.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by andrew on 1/22/16.
 */
public class DonationsDaoTest extends WebAppConfigurationAware {

    @Autowired
    DonationDao donationDao;

    @Autowired
    DonorInfoDao donorInfoDao;

    @Autowired
    ContactDao contactDao;

    @Autowired
    OrganizationDao organizationDao;

    @Autowired
    EventDao eventDao;

    private Donation donation;

    @After
    public void tearDown() {
        contactDao.deleteAll();
        donorInfoDao.deleteAll();
        donationDao.deleteAll();
        eventDao.deleteAll();
        organizationDao.deleteAll();
    }

    @Before
    public void setup() {
        donation = new Donation();
        donation.setAmount(100);
        donation.setBudgetItem("Budget Item");
        donation.setRestrictedToCategory("Restricted To");
        donation.setStandalone(true);
        donation.setAnonymous(true);
        donation.setDateOfDeposit(LocalDate.of(2016, 6, 10));
        donation.setDateOfReceipt(LocalDate.of(2016, 6, 7));

    }

    @Test
    public void testCreateDonation() {
        donationDao.save(donation);
        Donation fromDb = donationDao.findOne(donation.getId());
        assertEquals(donation.getAmount(), fromDb.getAmount());
        assertEquals(donation.getBudgetItem(), fromDb.getBudgetItem());
        assertEquals(donation.getRestrictedToCategory(), fromDb.getRestrictedToCategory());
        assertEquals(donation.isStandalone(), fromDb.isStandalone());
        assertEquals(donation.isAnonymous(), fromDb.isAnonymous());
        assertEquals(donation.getDateOfReceipt(), fromDb.getDateOfReceipt());
        assertEquals(donation.getDateOfDeposit(), fromDb.getDateOfDeposit());
    }

    @Test
    public void testDeleteDonation() {
        donationDao.save(donation);
        donationDao.delete(donation);
        Donation fromDb = donationDao.findOne(donation.getId());
        assertNull(fromDb);
    }

    @Test
    public void testCreateDonorInfo() {
        DonorInfo donorInfo = new DonorInfo();
        donorInfo.addDonation(donation);
        donorInfoDao.save(donorInfo);

        DonorInfo fromDb = donorInfoDao.findOne(donorInfo.getId());
        assertEquals(fromDb.getDonations().iterator().next(), donation);
    }

    @Test
    public void testDeleteDonorInfo() {
        DonorInfo donorInfo = new DonorInfo();
        donorInfo.addDonation(donation);
        donorInfoDao.save(donorInfo);

        donorInfoDao.delete(donorInfo);

        DonorInfo fromDb = donorInfoDao.findOne(donorInfo.getId());
        assertNull(fromDb);

        Donation relatedDonation = donationDao.findOne(donation.getId());
        assertNull(relatedDonation);
    }

    @Test
    public void testUpdateDonorInfo() {
        DonorInfo donorInfo = new DonorInfo();
        donorInfo.addDonation(donation);
        donorInfoDao.save(donorInfo);

        donation = donorInfo.getDonations().iterator().next();
        int newAmount = donation.getAmount() + 1;
        donation.setAmount(newAmount);

        donorInfoDao.save(donorInfo);
        donorInfo = donorInfoDao.findOne(donorInfo.getId());

        assertEquals(newAmount, donorInfo.getDonations().iterator().next().getAmount());
    }

    @Test
    public void testRemoveDonationFromDonorInfo() {
        DonorInfo donorInfo = new DonorInfo();
        donorInfo.addDonation(donation);
        donorInfoDao.save(donorInfo);

        donorInfo = donorInfoDao.findOne(donorInfo.getId());
        donorInfo.getDonations().remove(donation);
        donorInfoDao.save(donorInfo);

        donorInfo = donorInfoDao.findOne(donorInfo.getId());
        assertEquals(0, donorInfo.getDonations().size());
    }

    @Test
    public void testCreateContactWithDonations() {
        Contact c = new Contact();
        c.setFirstName("Test");
        c.setEmail("email@test.com");

        DonorInfo donorInfo = new DonorInfo();
        donorInfo.addDonation(donation);
        c.setDonorInfo(donorInfo);

        contactDao.save(c);

        Contact contactFromDb = contactDao.findOne(c.getId());
        Set<Donation> contactDonations = contactFromDb.getDonorInfo().getDonations();
        assertEquals(contactDonations.iterator().next(), donation);
    }

    @Test
    public void testDeleteContactWithDonations() {
        Contact c = new Contact();
        c.setFirstName("Test");
        c.setEmail("email@test.com");

        DonorInfo donorInfo = new DonorInfo();
        donorInfo.addDonation(donation);
        c.setDonorInfo(donorInfo);

        contactDao.save(c);
        Contact fromDb = contactDao.findOne(c.getId());
        assertNotNull(fromDb);
        contactDao.delete(c);

        fromDb = contactDao.findOne(c.getId());
        assertNull(fromDb);
        Donation donationFromDb = donationDao.findOne(donation.getId());
        assertNull(donationFromDb);
    }

    @Test
    public void testUpdateContactDonation() {
        Contact c = new Contact();
        c.setFirstName("Test");
        c.setEmail("email@test.com");

        DonorInfo donorInfo = new DonorInfo();
        donorInfo.addDonation(donation);
        c.setDonorInfo(donorInfo);

        contactDao.save(c);
        Contact fromDb = contactDao.findOne(c.getId());
        donation = fromDb.getDonorInfo().getDonations().iterator().next();
        int newAmount = donation.getAmount() + 1;
        donation.setAmount(newAmount);

        contactDao.save(fromDb);

        fromDb = contactDao.findOne(c.getId());
        assertEquals(newAmount, fromDb.getDonorInfo().getDonations().iterator().next().getAmount());

    }

    @Test
    public void testCreateOrganizationWithDonation() {
        Organization org = new Organization();
        org.setName("Org name");
        org.addDonation(donation);
        organizationDao.save(org);

        org = organizationDao.findOne(org.getId());
        assertEquals(donation, org.getDonations().iterator().next());
    }

    @Test
    public void testDeleteOrganizationWithDonation() {
        Organization org = new Organization();
        org.setName("Org name");
        org.addDonation(donation);
        organizationDao.save(org);

        org = organizationDao.findOne(org.getId());
        organizationDao.delete(org);

        org = organizationDao.findOne(org.getId());
        assertNull(org);

        donation = donationDao.findOne(donation.getId());
        assertNull(donation);
    }

    @Test
    public void testUpdateOrganizationDonation() {
        Organization org = new Organization();
        org.setName("Org name");
        org.addDonation(donation);
        organizationDao.save(org);

        org = organizationDao.findOne(org.getId());
        donation = org.getDonations().iterator().next();
        int newAmount = donation.getAmount() + 1;
        donation.setAmount(newAmount);
        organizationDao.save(org);

        org = organizationDao.findOne(org.getId());
        assertEquals(newAmount, org.getDonations().iterator().next().getAmount());

    }

    @Test
    public void testCreateEventWithDonation() {
        Event e = new Event();
        e.setName("Test Event");
        e.setDateHeld("2016-12-12");
        e.addDonation(donation);
        eventDao.save(e);

        e = eventDao.findOne(e.getId());
        assertEquals(donation, e.getDonations().iterator().next());
    }

    @Test
    public void testDeleteEventWithDonation() {
        Event e = new Event();
        e.setName("Test Event");
        e.setDateHeld("2016-12-12");
        e.addDonation(donation);
        eventDao.save(e);

        e = eventDao.findOne(e.getId());
        eventDao.delete(e);

        e = eventDao.findOne(e.getId());
        assertNull(e);

        donation = donationDao.findOne(donation.getId());
        assertNull(donation);

    }

    @Test
    public void testUpdateEventDonation() {
        Event e = new Event();
        e.setName("Test Event");
        e.setDateHeld("2016-12-12");
        e.addDonation(donation);
        eventDao.save(e);

        e = eventDao.findOne(e.getId());
        donation = e.getDonations().iterator().next();
        int newAmount = donation.getAmount() + 1;
        donation.setAmount(newAmount);
        eventDao.save(e);

        e = eventDao.findOne(e.getId());
        assertEquals(newAmount, e.getDonations().iterator().next().getAmount());
    }

    @Test
    @Transactional
    public void testPersistDonationByContactForEvent() {
        Event e = new Event();
        e.setName("Test Event");
        e.setDateHeld("2016-12-12");
        e.addDonation(donation);
        eventDao.save(e);

        e = eventDao.findOne(e.getId());
        donation = e.getDonations().iterator().next();

        Contact c = new Contact();
        c.setFirstName("Test");
        c.setEmail("email@test.com");

        DonorInfo donorInfo = new DonorInfo();
        donorInfo.addDonation(donation);
        c.setDonorInfo(donorInfo);

        contactDao.save(c);

        e = eventDao.findOne(e.getId());
        c = contactDao.findOne(c.getId());

        assertEquals(e.getDonations().iterator().next(), c.getDonorInfo().getDonations().iterator().next());

        /*Update*/
        donation = e.getDonations().iterator().next();
        int newAmount = donation.getAmount() + 1;
        donation.setAmount(newAmount);
        eventDao.save(e);

        c = contactDao.findOne(c.getId());
        donation = c.getDonorInfo().getDonations().iterator().next();
        assertEquals(newAmount, donation.getAmount());

        /*Delete*/
        eventDao.delete(e);
        contactDao.delete(c);
        donation = donationDao.findOne(donation.getId());
        assertNull(donation);
    }

}
