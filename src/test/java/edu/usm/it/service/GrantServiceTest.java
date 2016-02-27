package edu.usm.it.service;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Foundation;
import edu.usm.domain.Grant;
import edu.usm.domain.UserFileUpload;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.repository.UserFileUploadDao;
import edu.usm.service.FoundationService;
import edu.usm.service.GrantService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * Created by andrew on 2/16/16.
 */
public class GrantServiceTest extends WebAppConfigurationAware {

    @Autowired
    GrantService grantService;

    @Autowired
    FoundationService foundationService;

    @Autowired
    UserFileUploadDao fileDao;

    Foundation foundation;
    Grant grant;
    UserFileUpload userFileUpload;
    private int fileLength = 10;
    private byte[] fileData = new byte[fileLength];

    @Before
    public void setup() throws Exception{
        foundation = new Foundation("Test Foundation");
        foundationService.create(foundation);

        grant = new Grant("Test Grant", foundation);
        grant.setAmountAppliedFor(100);
        grant.setAmountReceived(100);
        grant.setDescription("A test grant");
        grant.setReportDeadline(LocalDate.of(2016, 7, 1));
        grant.setApplicationDeadline(LocalDate.of(2015, 3, 1));
        grant.setIntentDeadline(LocalDate.of(2015, 2, 1));
        grant.setStartPeriod(LocalDate.of(2015, 6, 1));
        grant.setEndPeriod(LocalDate.of(2016, 6, 1));
        grant.setRestriction("Administration expenses");

        userFileUpload = new UserFileUpload();
        for (int i = 0; i < fileLength; i++) {
            fileData[i] = (byte)i;
        }
        userFileUpload.setFileContent(fileData);
        userFileUpload.setFileType(".properties");
        userFileUpload.setFileName("application.properties");
        userFileUpload.setDescription("A test file");
        grant.addFileUpload(userFileUpload);
    }

    @After
    public void teardown() {
        grantService.deleteAll();
        foundationService.deleteAll();
    }

    @Test
    public void testCreateGrant() throws ConstraintViolation{
        grantService.create(grant);

        grant = grantService.findById(grant.getId());
        assertNotNull(grant);
        assertTrue(grant.getFileUploads().contains(userFileUpload));
        foundation = foundationService.findById(foundation.getId());
        assertTrue(foundation.getGrants().contains(grant));
    }

    @Test
    public void testUpdateGrant() throws ConstraintViolation{
        grantService.create(grant);
        grant = grantService.findById(grant.getId());
        String newName = "New Grant Name";
        grant.setName(newName);
        grantService.update(grant);

        grant = grantService.findById(grant.getId());
        assertEquals(newName, grant.getName());
    }

    @Test
    public void testDeleteGrant() throws ConstraintViolation{
        testCreateGrant();
        grantService.delete(grant);

        grant = grantService.findById(grant.getId());
        assertNull(grant);

        foundation = foundationService.findById(foundation.getId());
        assertTrue(foundation.getGrants().isEmpty());

        userFileUpload = fileDao.findOne(userFileUpload.getId());
        assertNull(userFileUpload);
    }

    @Test(expected = ConstraintViolation.class)
    public void testCreateGrantNullName() throws ConstraintViolation{
        grant.setName(null);
        grantService.create(grant);
    }

}
