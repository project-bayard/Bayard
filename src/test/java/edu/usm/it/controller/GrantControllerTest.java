package edu.usm.it.controller;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Foundation;
import edu.usm.domain.Grant;
import edu.usm.domain.Views;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.dto.DtoTransformer;
import edu.usm.dto.GrantDto;
import edu.usm.service.FoundationService;
import edu.usm.service.GrantService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ConstantException;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by andrew on 2/17/16.
 */

public class GrantControllerTest extends WebAppConfigurationAware{

    @Autowired
    GrantService grantService;

    @Autowired
    FoundationService foundationService;

    static final String GRANTS_BASE_URL = "/grants/";

    Grant grantOne;
    Grant grantTwo;
    Foundation foundation;

    @Before
    public void setup() throws ConstraintViolation {
        foundation = new Foundation("Test Foundation");

        grantOne = new Grant("Grant One", foundation);
        grantOne.setDescription("A test grant");
        grantOne.setAmountAppliedFor(300);
        grantOne.setAmountReceived(300);
        grantOne.setReportDeadline(LocalDate.of(2016, 7, 1));
        grantOne.setApplicationDeadline(LocalDate.of(2015, 3, 1));
        grantOne.setIntentDeadline(LocalDate.of(2015, 2, 1));
        grantOne.setStartPeriod(LocalDate.of(2015, 6, 1));
        grantOne.setEndPeriod(LocalDate.of(2016, 6, 1));
        grantOne.setRestriction("Administration expenses");

        grantTwo = new Grant("Grant Two", foundation);
        grantTwo.setAmountAppliedFor(1000);
        grantTwo.setAmountReceived(400);
        grantTwo.setReportDeadline(LocalDate.of(2016, 7, 2));
        grantTwo.setApplicationDeadline(LocalDate.of(2015, 4, 1));
        grantTwo.setIntentDeadline(LocalDate.of(2015, 2, 5));
        grantTwo.setStartPeriod(LocalDate.of(2015, 6, 2));
        grantTwo.setEndPeriod(LocalDate.of(2016, 6, 3));

    }

    @After
    public void teardown() {
        foundationService.deleteAll();
    }

    @Test
    public void testGetGrants() throws Exception{
        foundation.addGrant(grantOne);
        foundation.addGrant(grantTwo);
        foundationService.create(foundation);
        foundation = foundationService.findById(foundation.getId());

        assertEquals(2, grantService.findAll().size());
        BayardTestUtilities.performEntityGetMultiple(Views.GrantList.class, GRANTS_BASE_URL, mockMvc, grantOne, grantTwo);
    }

    @Test
    public void testGetGrant() throws Exception {
        foundation.addGrant(grantOne);
        foundationService.create(foundation);
        BayardTestUtilities.performEntityGetSingle(Views.GrantDetails.class, GRANTS_BASE_URL+grantOne.getId(), mockMvc, grantOne);
    }

    @Test
    public void testDeleteGrant() throws Exception {
        foundation.addGrant(grantOne);
        foundationService.create(foundation);
        foundation = foundationService.findById(foundation.getId());
        grantOne = foundation.getGrants().iterator().next();

        String url = GRANTS_BASE_URL+grantOne.getId();

        BayardTestUtilities.performEntityDelete(url, mockMvc);

        foundation = foundationService.findById(foundation.getId());
        assertTrue(foundation.getGrants().isEmpty());

        Set<Grant> shouldBeEmpty = grantService.findAll();
        assertTrue(shouldBeEmpty.isEmpty());

    }

    @Test
    public void testUpdateGrantDetails() throws Exception {
        foundation.addGrant(grantOne);
        foundationService.create(foundation);
        foundation = foundationService.findById(foundation.getId());
        grantOne = foundation.getGrants().iterator().next();

        String updatedName = "Name Updated";
        LocalDate updatedDate = LocalDate.of(2016, 6, 6);
        GrantDto dto = DtoTransformer.fromEntity(grantOne);
        dto.setName(updatedName);
        dto.setApplicationDeadline(updatedDate);

        String url = GRANTS_BASE_URL+grantOne.getId();
        BayardTestUtilities.performEntityPut(url, dto, mockMvc);

        foundation = foundationService.findById(foundation.getId());
        Grant fromDb = foundation.getGrants().iterator().next();
        assertEquals(updatedName, fromDb.getName());
        assertEquals(updatedDate, fromDb.getApplicationDeadline());
    }

}
