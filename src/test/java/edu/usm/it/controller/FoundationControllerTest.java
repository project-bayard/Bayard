package edu.usm.it.controller;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Foundation;
import edu.usm.domain.Grant;
import edu.usm.domain.InteractionRecord;
import edu.usm.domain.Views;

import edu.usm.service.FoundationService;
import edu.usm.service.GrantService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * Created by andrew on 2/12/16.
 */
public class FoundationControllerTest extends WebAppConfigurationAware {

    @Autowired
    FoundationService foundationService;

    @Autowired
    GrantService grantService;

    final String FOUNDATIONS_BASE_URL = "/foundations/";

    Foundation foundation;

    @Before
    public void setup() {
        foundation = new Foundation("Test Foundation");
        foundation.setCurrentGrantor(true);
        foundation.setPhoneNumber("123-123-1233");
        foundation.setAddress("123 Lane Ave");
        foundation.setWebsite("foundation.com");
        foundation.setPrimaryContactEmail("email@email.com");
        foundation.setPrimaryContactName("Point Person");
        foundation.setPrimaryContactEmail("point@person.com");
        foundation.setPrimaryContactTitle("Manager");
        foundation.setPrimaryContactPhone("123-123-1111");
        foundation.setSecondaryContactName("Secondary Point Person");
        foundation.setSecondaryContactTitle("Assistant Regional Manager");
        foundation.setSecondaryContactEmail("second@email.com");
        foundation.setSecondaryContactPhone("123-123-0000");

    }

    @After
    public void teardown() {
        foundationService.deleteAll();
    }

    @Test
    public void testCreateFoundation() throws Exception {
        BayardTestUtilities.performEntityPost(FOUNDATIONS_BASE_URL, foundation, mockMvc);
        Foundation fromDb = foundationService.findAll().iterator().next();
        assertNotNull(fromDb);
        assertEquals(foundation.getName(), fromDb.getName());
    }

    @Test
    public void testGetFoundation() throws Exception {
        foundationService.create(foundation);
        BayardTestUtilities.performEntityGetSingle(Views.FoundationDetails.class, FOUNDATIONS_BASE_URL + foundation.getId(), mockMvc, foundation);
    }

    @Test
    public void testGetAllFoundations() throws Exception {
        foundationService.create(foundation);
        Foundation secondFoundation = makeSecondFoundation();
        foundationService.create(secondFoundation);

        BayardTestUtilities.performEntityGetMultiple(Views.FoundationList.class, FOUNDATIONS_BASE_URL, mockMvc, foundation, secondFoundation);
    }

    private Foundation makeSecondFoundation() {
        Foundation secondFoundation = new Foundation("Second Foundation");
        secondFoundation.setPrimaryContactEmail("primary@contact.email");
        secondFoundation.setAddress("987 Ave Americas");
        secondFoundation.setPrimaryContactName("Another Primary Contact");
        secondFoundation.setPhoneNumber("123-321-2121");
        secondFoundation.setWebsite("second.com");
        return secondFoundation;
    }

    @Test
    public void testUpdateFoundation() throws Exception{
        foundationService.create(foundation);
        foundation = foundationService.findById(foundation.getId());
        assertNotNull(foundation);

        String newFoundationName = "New Name for the Foundation";
        foundation.setName(newFoundationName);

        BayardTestUtilities.performEntityPut(FOUNDATIONS_BASE_URL + foundation.getId(), foundation, mockMvc);

        Foundation fromDb = foundationService.findById(foundation.getId());
        assertEquals(newFoundationName, fromDb.getName());
    }

    @Test
    public void testDeleteFoundation() throws Exception {
        foundationService.create(foundation);
        foundation = foundationService.findById(foundation.getId());
        assertNotNull(foundation);

        BayardTestUtilities.performEntityDelete(FOUNDATIONS_BASE_URL + foundation.getId(), mockMvc);

        foundation = foundationService.findById(foundation.getId());
        assertNull(foundation);
    }

    @Test
    public void testCreateGrant() throws Exception {
        foundationService.create(foundation);
        Grant grant = new Grant();
        grant.setName("Test Grant");
        grant.setApplicationDeadline(LocalDate.of(2016, 6, 6));
        grant.setIntentDeadline(LocalDate.now());
        grant.setReportDeadline(LocalDate.of(2017, 6, 6));
        grant.setAmountReceived(100);

        BayardTestUtilities.performEntityPost(FOUNDATIONS_BASE_URL+foundation.getId()+"/grants", grant, mockMvc);

        foundation = foundationService.findById(foundation.getId());
        assertEquals(1, foundation.getGrants().size());

        Grant fromDb = grantService.findById(foundation.getGrants().iterator().next().getId());
        assertEquals(grant.getName(), fromDb.getName());
        assertEquals(grant.getApplicationDeadline(), fromDb.getApplicationDeadline());
        assertEquals(grant.getIntentDeadline(), fromDb.getIntentDeadline());
        assertEquals(grant.getReportDeadline(), fromDb.getReportDeadline());
        assertEquals(grant.getAmountReceived(), fromDb.getAmountReceived());
    }

    @Test
    public void testGetFoundationGrants() throws Exception {
        Grant grantOne = new Grant("Grant One", foundation);
        Grant grantTwo = new Grant("Grant Two", foundation);
        foundation.addGrant(grantOne);
        foundation.addGrant(grantTwo);
        foundationService.create(foundation);

        foundation = foundationService.findById(foundation.getId());
        assertEquals(2, foundation.getGrants().size());
        String url = FOUNDATIONS_BASE_URL+foundation.getId()+"/grants";
        BayardTestUtilities.performEntityGetMultiple(Views.GrantList.class, url, mockMvc, grantOne, grantTwo);

    }

    @Test
    public void testCreateInteractionRecord() throws Exception {
        foundationService.create(foundation);
        InteractionRecord record = new InteractionRecord("Point person", LocalDate.now(), "Call", foundation);
        record.setNotes("Some notes");
        record.setRequiresFollowUp(false);
        String url = FOUNDATIONS_BASE_URL+foundation.getId()+"/interactions";
        BayardTestUtilities.performEntityPost(url, record, mockMvc);

        foundation = foundationService.findById(foundation.getId());
        InteractionRecord fromDb = foundation.getInteractionRecords().iterator().next();
        assertEquals(record.getInteractionType(), fromDb.getInteractionType());
        assertEquals(record.getPersonContacted(), fromDb.getPersonContacted());
        assertEquals(record.getDateOfInteraction(), fromDb.getDateOfInteraction());
        assertEquals(record.getNotes(), fromDb.getNotes());
        assertEquals(record.isRequiresFollowUp(), fromDb.isRequiresFollowUp());

    }

    @Test
    public void testGetInteractionRecords() throws Exception {
        InteractionRecord record = new InteractionRecord("Point person", LocalDate.now(), "Call", foundation);
        record.setNotes("Some notes");
        record.setRequiresFollowUp(true);
        InteractionRecord anotherRecord = new InteractionRecord("Head of Foundation", LocalDate.of(2016, 1, 1), "Email", foundation);
        record.setNotes("A follow up to the first interaction");
        record.setRequiresFollowUp(false);
        foundation.addInteractionRecord(record);
        foundation.addInteractionRecord(anotherRecord);
        foundationService.create(foundation);

        String url = FOUNDATIONS_BASE_URL+foundation.getId()+"/interactions";
        BayardTestUtilities.performEntityGetMultiple(Views.InteractionRecordList.class, url, mockMvc, record, anotherRecord);
    }

}
