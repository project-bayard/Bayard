package edu.usm.it.controller;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.BudgetItem;
import edu.usm.domain.Donation;
import edu.usm.domain.Views;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.dto.DtoTransformer;
import edu.usm.service.DonationService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by andrew on 2/25/16.
 */
public class DonationControllerTest extends WebAppConfigurationAware {

    @Autowired
    DonationService donationService;

    Donation donation;
    BudgetItem budgetItem;

    final static String DONATIONS_BASE_URL = "/donations/";

    @Before
    public void setup() {
        donation = new Donation();
        donation.setAmount(20);
        donation.setAnonymous(true);
        donation.setBudgetItem(budgetItem);
        donation.setDateOfDeposit(LocalDate.of(2015, 12, 12));
        donation.setDateOfReceipt(LocalDate.of(2015, 11, 11));
        donation.setMethod("Credit Card");
        donation.setRestrictedToCategory("Office supplies");
        donation.setStandalone(true);
    }

    @After
    public void tearDown() {
        donationService.deleteAllBudgetItems();
        donationService.deleteAll();
    }

    @Test
    public void testCreateDonation() throws Exception {
        budgetItem = new BudgetItem("Misc");
        donationService.createBudgetItem(budgetItem);
        donation.setBudgetItem(budgetItem);

        BayardTestUtilities.performEntityPost(DONATIONS_BASE_URL, DtoTransformer.fromEntity(donation), mockMvc);

        Donation fromDb = donationService.findAll().iterator().next();
        assertEquals(donation.getRestrictedToCategory(), fromDb.getRestrictedToCategory());
        donation = donationService.findById(fromDb.getId());
        assertEquals(donation.getDateOfDeposit(), fromDb.getDateOfDeposit());
        assertEquals(donation.getBudgetItemName(), fromDb.getBudgetItemName());
    }

    @Test
    public void testUpdateDonation() throws Exception {
        budgetItem = new BudgetItem("Misc");
        donationService.createBudgetItem(budgetItem);
        donation.setBudgetItem(budgetItem);
        donationService.create(donation);
        donation = donationService.findById(donation.getId());

        int newDonationAmount = donation.getAmount() + 10;
        donation.setAmount(newDonationAmount);
        BudgetItem newBudgetItem = new BudgetItem("New Budget Item");
        donationService.createBudgetItem(newBudgetItem);
        donation.setBudgetItem(newBudgetItem);
        BayardTestUtilities.performEntityPut(DONATIONS_BASE_URL + donation.getId(), DtoTransformer.fromEntity(donation), mockMvc);

        Donation fromDb = donationService.findById(donation.getId());
        assertEquals(donation, fromDb);
        assertEquals(donation.getBudgetItem().getName(), fromDb.getBudgetItem().getName());
    }

    @Test
    public void testGetDonation() throws Exception {
        donationService.create(donation);

        BayardTestUtilities.performEntityGetSingle(Views.DonationDetails.class, DONATIONS_BASE_URL + donation.getId(), mockMvc, donation);
    }

    @Test(expected = NullDomainReference.NullDonation.class)
    public void testDeleteDonation() throws Exception {
        String id = donationService.create(donation);
        donation = donationService.findById(id);

        BayardTestUtilities.performEntityDelete(DONATIONS_BASE_URL + donation.getId(), mockMvc);
        donation = donationService.findById(donation.getId());
    }

    @Test
    public void testGetAllBudgetItems() throws Exception{
        budgetItem = new BudgetItem("Misc");
        donationService.createBudgetItem(budgetItem);

        mockMvc.perform(get(DONATIONS_BASE_URL+"budgetitems")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(budgetItem.getId())))
                .andExpect(jsonPath("$.[0].name", is(budgetItem.getName())));
    }

    @Test
    public void testCreateBudgetItem() throws Exception {
        BudgetItem newBudgetItem = new BudgetItem("New Budget Item");
        BayardTestUtilities.performEntityPost(DONATIONS_BASE_URL + "budgetitems", newBudgetItem, mockMvc);

        Set<BudgetItem> budgetItems = donationService.findAllBudgetItems();
        assertEquals(newBudgetItem.getName(), budgetItems.iterator().next().getName());
    }


    @Test
    public void testDeleteBudgetItem() throws Exception {
        budgetItem = new BudgetItem("Misc");
        donationService.createBudgetItem(budgetItem);

        BayardTestUtilities.performEntityDelete(DONATIONS_BASE_URL + "budgetitems/" + budgetItem.getId(), mockMvc);

        budgetItem = donationService.findBudgetItem(budgetItem.getId());
        assertNull(budgetItem);
    }

    @Test
    public void testUpdateBudgetItemName() throws Exception {
        budgetItem = new BudgetItem("Misc");
        donationService.createBudgetItem(budgetItem);

        budgetItem.setName("Updated Name");
        BayardTestUtilities.performEntityPut(DONATIONS_BASE_URL + "budgetitems/" + budgetItem.getId(), budgetItem, mockMvc);

        BudgetItem fromDb = donationService.findBudgetItem(budgetItem.getId());
        assertEquals(budgetItem.getName(), fromDb.getName());
    }

    @Test
    public void testGetDonationsByDepositRange() throws Exception {
        donationService.create(donation);
        Donation secondDonation = new Donation(100, "Credit Card", donation.getDateOfReceipt(), donation.getDateOfDeposit());
        donationService.create(secondDonation);
        LocalDate from = donation.getDateOfDeposit().minus(7, ChronoUnit.DAYS);
        LocalDate to = donation.getDateOfDeposit().plus(7, ChronoUnit.DAYS);

        mockMvc.perform(get(DONATIONS_BASE_URL + "/bydepositdate")
                .param("from", from.toString())
                .param("to", to.toString())
                .param("page.page", "0")
                .param("page.size", "20")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void testGetDonationsByReceiptRange() throws Exception {
        donationService.create(donation);
        Donation secondDonation = new Donation(100, "Credit Card", donation.getDateOfReceipt(), donation.getDateOfDeposit());
        donationService.create(secondDonation);
        LocalDate from = donation.getDateOfDeposit().minus(7, ChronoUnit.DAYS);
        LocalDate to = donation.getDateOfDeposit().plus(7, ChronoUnit.DAYS);

        mockMvc.perform(get(DONATIONS_BASE_URL + "/byreceiptdate")
                .param("from", from.toString())
                .param("to", to.toString())
                .param("page.page", "0")
                .param("page.size", "20")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void testGetDonationsByBudgetItem() throws Exception {
        budgetItem = new BudgetItem("Test budget item");
        donationService.createBudgetItem(budgetItem);
        donation.setBudgetItem(budgetItem);
        donationService.create(donation);

        mockMvc.perform(get(DONATIONS_BASE_URL + "/bybudgetitem")
                .param("item", budgetItem.getId())
                .param("page.page", "0")
                .param("page.size", "20")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

}
