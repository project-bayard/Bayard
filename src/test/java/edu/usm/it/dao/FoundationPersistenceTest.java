package edu.usm.it.dao;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Foundation;
import edu.usm.domain.Grant;
import edu.usm.domain.InteractionRecord;
import edu.usm.domain.UserFileUpload;
import edu.usm.repository.FoundationDao;
import edu.usm.repository.GrantDao;
import edu.usm.repository.InteractionRecordDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.TransactionSystemException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * Created by andrew on 1/24/16.
 */
public class FoundationPersistenceTest extends WebAppConfigurationAware {

    @Autowired
    private FoundationDao foundationDao;

    @Autowired
    private GrantDao grantDao;

    @Autowired
    private InteractionRecordDao interactionRecordDao;

    private Foundation foundation;
    private Grant grant;
    private InteractionRecord interactionRecord;
    private UserFileUpload userFileUpload;

    private int fileLength = 10;
    private byte[] fileData = new byte[fileLength];


    @Before
    public void setup() throws Exception {

        foundation = new Foundation();
        foundation.setName("Test Foundation");

        grant = new Grant();
        grant.setName("Test Grant");
        grant.setFoundation(foundation);
        grant.setAmountAppliedFor(100);
        grant.setAmountReceived(50);

        userFileUpload = new UserFileUpload();
        for (int i = 0; i < fileLength; i++) {
            fileData[i] = (byte)i;
        }
        userFileUpload.setFileContent(fileData);
        userFileUpload.setFileType(".properties");
        userFileUpload.setFileName("application.properties");
        userFileUpload.setDescription("A test file");
        grant.addFileUpload(userFileUpload);

        foundation.addGrant(grant);

        interactionRecord = new InteractionRecord();
        interactionRecord.setDateOfInteraction(LocalDate.now());
        interactionRecord.setFoundation(foundation);
        interactionRecord.setPersonContacted("John Smith");
        interactionRecord.setInteractionType("Interaction Type");
        foundation.addInteractionRecord(interactionRecord);

    }

    @After
    public void teardown() {
        foundationDao.deleteAll();
        grantDao.deleteAll();
        interactionRecordDao.deleteAll();
    }

    @Test
    public void testCreateFoundation() {
        foundationDao.save(foundation);

        foundation = foundationDao.findOne(foundation.getId());
        assertNotNull(foundation);
        assertEquals(interactionRecord, foundation.getInteractionRecords().iterator().next());
        assertEquals(grant, foundation.getGrants().iterator().next());
        assertEquals(userFileUpload, grant.getFileUploads().iterator().next());
        assertEquals(fileData, grant.getFileUploads().iterator().next().getFileContent());
    }

    @Test
    public void testCreateFoundationMultipleGrants() {
        foundationDao.save(foundation);
        foundation = foundationDao.findOne(foundation.getId());
        Grant secondGrant = new Grant("Second Grant", foundation);
        foundation.getGrants().add(secondGrant);
        foundationDao.save(foundation);

        foundation = foundationDao.findOne(foundation.getId());
        assertEquals(2, foundation.getGrants().size());
    }

    @Test(expected = DataAccessException.class)
    public void testCreateFoundationDuplicateName() {
        foundationDao.save(foundation);
        foundationDao.save(new Foundation(foundation.getName()));
    }

    @Test(expected = TransactionSystemException.class)
    public void testCreateFoundationNoName() {
        foundation.setName(null);
        foundationDao.save(foundation);
    }

    @Test
    public void testDeleteFoundation() {
        foundationDao.save(foundation);
        foundation = foundationDao.findOne(foundation.getId());

        foundationDao.delete(foundation);

        foundation = foundationDao.findOne(foundation.getId());
        assertNull(foundation);

        grant = grantDao.findOne(grant.getId());
        assertNull(grant);

        interactionRecord = interactionRecordDao.findOne(interactionRecord.getId());
        assertNull(interactionRecord);
    }

    @Test
    public void testCascadeUpdateGrant() {
        foundationDao.save(foundation);
        foundation = foundationDao.findOne(foundation.getId());

        grant = foundation.getGrants().iterator().next();
        int newAmount = grant.getAmountAppliedFor() + 1;
        grant.setAmountAppliedFor(newAmount);

        foundationDao.save(foundation);

        foundation = foundationDao.findOne(foundation.getId());
        grant = foundation.getGrants().iterator().next();
        assertEquals(newAmount, grant.getAmountAppliedFor());

    }

    @Test
    public void testUpdateGrant() {
        foundationDao.save(foundation);

        grant = grantDao.findOne(grant.getId());
        int newAmount = grant.getAmountAppliedFor() + 1;
        grant.setAmountAppliedFor(newAmount);

        grantDao.save(grant);
        grant = grantDao.findOne(grant.getId());

        assertEquals(newAmount, grant.getAmountAppliedFor());
    }

    @Test
    public void testCascadeUpdateInteractionRecord() {
        foundationDao.save(foundation);
        foundation = foundationDao.findOne(foundation.getId());

        interactionRecord = foundation.getInteractionRecords().iterator().next();
        String newInteractionType = "Test Type";
        interactionRecord.setInteractionType(newInteractionType);

        foundationDao.save(foundation);

        foundation = foundationDao.findOne(foundation.getId());
        interactionRecord = foundation.getInteractionRecords().iterator().next();
        assertEquals(newInteractionType, interactionRecord.getInteractionType());
    }

    @Test
    public void testUpdateInteractionRecord() {
        foundationDao.save(foundation);
        interactionRecord = interactionRecordDao.findOne(interactionRecord.getId());

        String newInteractionType = "Test Type";
        interactionRecord.setInteractionType(newInteractionType);
        interactionRecordDao.save(interactionRecord);

        interactionRecord = interactionRecordDao.findOne(interactionRecord.getId());
        assertEquals(newInteractionType, interactionRecord.getInteractionType());
    }

    @Test
    public void testCascadeUpdateUserFileUpload() {
        foundationDao.save(foundation);
        foundation = foundationDao.findOne(foundation.getId());

        userFileUpload = foundation.getGrants().iterator().next().getFileUploads().iterator().next();
        byte []persistedFileData = userFileUpload.getFileContent();

        assertArrayEquals(fileData, persistedFileData);

        byte newByteValue = (byte) 500;
        persistedFileData[0] = newByteValue;
        userFileUpload.setFileContent(persistedFileData);
        foundationDao.save(foundation);

        foundation = foundationDao.findOne(foundation.getId());
        userFileUpload = foundation.getGrants().iterator().next().getFileUploads().iterator().next();
        assertArrayEquals(persistedFileData, userFileUpload.getFileContent());

    }

}
