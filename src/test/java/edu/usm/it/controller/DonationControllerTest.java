package edu.usm.it.controller;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Donation;
import edu.usm.domain.Views;
import edu.usm.service.DonationService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by andrew on 2/25/16.
 */
public class DonationControllerTest extends WebAppConfigurationAware {

    @Autowired
    DonationService donationService;

    Donation donation;

    final static String DONATIONS_BASE_URL = "/donations/";

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
    }

    @After
    public void tearDown() {
        donationService.deleteAll();
    }

    @Test
    public void testCreateDonation() throws Exception {
        BayardTestUtilities.performEntityPost(DONATIONS_BASE_URL, donation, mockMvc);

        Donation fromDb = donationService.findAll().iterator().next();
        assertEquals(donation.getRestrictedToCategory(), fromDb.getRestrictedToCategory());
        assertEquals(donation.getDateOfDeposit(), fromDb.getDateOfDeposit());
    }

    @Test
    public void testUpdateDonation() throws Exception {
        donationService.create(donation);
        donation = donationService.findById(donation.getId());

        int newDonationAmount = donation.getAmount() + 10;
        donation.setAmount(newDonationAmount);
        BayardTestUtilities.performEntityPut(DONATIONS_BASE_URL+donation.getId(), donation, mockMvc);

        Donation fromDb = donationService.findById(donation.getId());
        assertEquals(donation, fromDb);
    }

    @Test
    public void testGetDonation() throws Exception {
        donationService.create(donation);

        BayardTestUtilities.performEntityGetSingle(Views.DonationDetails.class, DONATIONS_BASE_URL+donation.getId(), mockMvc, donation);
    }

    @Test
    public void testDeleteDonation() throws Exception {
        String id = donationService.create(donation);
        donation = donationService.findById(id);

        BayardTestUtilities.performEntityDelete(DONATIONS_BASE_URL + donation.getId(), mockMvc);
        donation = donationService.findById(donation.getId());
        assertNull(donation);
    }

}
