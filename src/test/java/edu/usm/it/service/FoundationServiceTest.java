package edu.usm.it.service;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Foundation;
import edu.usm.domain.Grant;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.dto.GrantDto;
import edu.usm.service.FoundationService;
import edu.usm.service.GrantService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ConcurrentModificationException;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by andrew on 2/12/16.
 */
public class FoundationServiceTest extends WebAppConfigurationAware{

    @Autowired
    FoundationService foundationService;

    @Autowired
    GrantService grantService;

    Foundation foundation;
    Grant grant;

    @Before
    public void setup() {
        foundation = new Foundation("Foundation Name");
        foundation.setAddress("123 Lane Ave");
        foundation.setCurrentGrantor(false);
        foundation.setPhoneNumber("123-321-412");

        grant = new Grant();
        grant.setName("Test Grant");
        grant.setFoundation(foundation);
        foundation.getGrants().add(grant);
    }

    @After
    public void teardown() {
        foundationService.deleteAll();
    }

    @Test
    public void testCreateFoundation() throws ConstraintViolation{
        foundationService.create(foundation);
        foundation = foundationService.findById(foundation.getId());
        assertNotNull(foundation);
        assertTrue(foundation.getGrants().contains(grant));
    }

    @Test
    public void testCreateFoundationMultipleGrants() throws ConstraintViolation {
        Grant secondGrant = new Grant("Second Grant", foundation);
        foundation.getGrants().add(secondGrant);
        testCreateFoundation();
        assertEquals(2, foundation.getGrants().size());
    }

    @Test
    public void testUpdateFoundation() throws ConstraintViolation{
        foundationService.create(foundation);
        foundation = foundationService.findById(foundation.getId());
        String newName = "New Name";
        foundation.setName(newName);
        foundationService.update(foundation);
        foundation = foundationService.findById(foundation.getId());
        assertEquals(newName, foundation.getName());
    }

    @Test
    public void testDeleteFoundation() throws ConstraintViolation{
        foundationService.create(foundation);
        foundation = foundationService.findById(foundation.getId());
        assertNotNull(foundation);

        foundationService.delete(foundation);
        foundation = foundationService.findById(foundation.getId());
        assertNull(foundation);

        Set<Grant> shouldBeEmpty = grantService.findAll();
        assertTrue(shouldBeEmpty.isEmpty());

    }

    @Test
    public void testDeleteAll() throws ConstraintViolation{
        foundationService.create(foundation);
        Foundation second = new Foundation("Second Foundation");
        foundationService.create(second);
        foundation = foundationService.findById(foundation.getId());
        second = foundationService.findById(second.getId());
        assertNotNull(foundation);
        assertNotNull(second);

        foundationService.deleteAll();
        Set<Foundation> shouldBeEmpty = foundationService.findAll();
        assertTrue(shouldBeEmpty.isEmpty());

        Set<Grant> shouldAlsoBeEmpty = grantService.findAll();
        assertTrue(shouldAlsoBeEmpty.isEmpty());
    }

    @Test
    public void testCreateGrant() throws ConstraintViolation {
        foundation.getGrants().remove(grant);
        foundationService.create(foundation);
        foundation = foundationService.findById(foundation.getId());
        assertTrue(foundation.getGrants().isEmpty());

        foundationService.createGrant(foundation, grant);
        foundation = foundationService.findById(foundation.getId());

        assertEquals(grant.getName(), foundation.getGrants().iterator().next().getName());
    }

    @Test
    public void testCreateGrantFromDto() throws ConstraintViolation {
        foundation.getGrants().remove(grant);
        foundationService.create(foundation);
        foundation = foundationService.findById(foundation.getId());

        GrantDto dto = new GrantDto();
        dto.setName("Test Grant");
        dto.setAmountAppliedFor(100);
        dto.setStartPeriod(LocalDate.now());
        dto.setApplicationDeadline(LocalDate.now());
        foundationService.createGrant(foundation, dto);

        foundation = foundationService.findById(foundation.getId());
        Grant fromDb = foundation.getGrants().iterator().next();

        assertEquals(dto.getName(), fromDb.getName());
        assertEquals(dto.getAmountAppliedFor(), fromDb.getAmountAppliedFor());
        assertEquals(dto.getStartPeriod(), fromDb.getStartPeriod());
        assertEquals(dto.getApplicationDeadline(), fromDb.getApplicationDeadline());

    }

    @Test
    public void testDeleteGrant() throws ConstraintViolation {
        foundationService.create(foundation);
        foundation = foundationService.findById(foundation.getId());
        grant = grantService.findById(grant.getId());

        foundationService.deleteGrant(foundation, grant);

        foundation = foundationService.findById(foundation.getId());
        assertTrue(foundation.getGrants().isEmpty());

        grant = grantService.findById(grant.getId());
        assertNull(grant);
    }

}
