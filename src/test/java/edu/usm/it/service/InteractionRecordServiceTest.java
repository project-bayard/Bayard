package edu.usm.it.service;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Foundation;
import edu.usm.domain.InteractionRecord;
import edu.usm.domain.UserFileUpload;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.repository.UserFileUploadDao;
import edu.usm.service.FoundationService;
import edu.usm.service.InteractionRecordService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by andrew on 2/18/16.
 */
public class InteractionRecordServiceTest extends WebAppConfigurationAware {

    @Autowired
    InteractionRecordService recordService;

    @Autowired
    FoundationService foundationService;

    @Autowired
    UserFileUploadDao dao;

    InteractionRecord record;
    Foundation f;

    UserFileUpload userFileUpload;
    int fileLength = 10;
    byte[] fileData = new byte[fileLength];

    @Before
    public void setup() throws ConstraintViolation{
        f = new Foundation("Test Foundation");
        foundationService.create(f);

        userFileUpload = new UserFileUpload();
        for (int i = 0; i < fileLength; i++) {
            fileData[i] = (byte)i;
        }
        userFileUpload.setFileContent(fileData);
        userFileUpload.setFileType(".properties");
        userFileUpload.setFileName("application.properties");
        userFileUpload.setDescription("A test file");

        record = new InteractionRecord("Contact Person", LocalDate.now(), "Call", f);
        record.setNotes("Some notes of the interaction");
        record.getFileUploads().add(userFileUpload);
    }

    @After
    public void teardown() {
        foundationService.deleteAll();
    }

    @Test
    public void testCreateInteraction() throws ConstraintViolation {
        recordService.create(record);
        record = recordService.findById(record.getId());
        assertNotNull(record);
        assertEquals(1, record.getFileUploads().size());

    }

    @Test
    public void testDeleteInteraction() throws ConstraintViolation {
        recordService.create(record);
        record = recordService.findById(record.getId());
        recordService.delete(record);
        record = recordService.findById(record.getId());
        assertNull(record);
        assertNull(dao.findOne(userFileUpload.getId()));
    }

    @Test
    public void testUpdateInteraction() throws ConstraintViolation {
        recordService.create(record);
        record = recordService.findById(record.getId());
        String newNotes = "Some new additional notes";
        record.setNotes(newNotes);
        recordService.update(record);

        record = recordService.findById(record.getId());
        assertEquals(newNotes, record.getNotes());
    }

    @Test(expected = ConstraintViolation.class)
    public void testCreateInteractionNoType() throws ConstraintViolation {
        record.setInteractionType(null);
        recordService.create(record);
    }

    @Test(expected = ConstraintViolation.class)
    public void testCreateInteractionNoFoundation() throws ConstraintViolation {
        record.setFoundation(null);
        recordService.create(record);
    }

    @Test(expected = ConstraintViolation.class)
    public void testUpdateInteractionNoDate() throws ConstraintViolation {
        recordService.create(record);
        record = recordService.findById(record.getId());
        record.setDateOfInteraction(null);
        recordService.update(record);

    }


}
