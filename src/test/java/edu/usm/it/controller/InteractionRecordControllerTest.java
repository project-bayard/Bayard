package edu.usm.it.controller;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Foundation;
import edu.usm.domain.InteractionRecord;
import edu.usm.domain.Views;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.dto.DtoTransformer;
import edu.usm.dto.InteractionRecordDto;
import edu.usm.service.FoundationService;
import edu.usm.service.InteractionRecordService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * Created by andrew on 2/18/16.
 */
public class InteractionRecordControllerTest extends WebAppConfigurationAware {


    final static String INTERACTIONS_BASE_URL = "/interactions/";

    @Autowired
    InteractionRecordService interactionService;

    @Autowired
    FoundationService foundationService;

    Foundation foundation;
    InteractionRecord record;

    @Before
    public void setup() throws ConstraintViolation {
        foundation = new Foundation("Test foundation");
        foundationService.create(foundation);

        record = new InteractionRecord("Person X", LocalDate.now(), "Call", foundation);
        record.setNotes("Notes from the interaction");
        record.setRequiresFollowUp(true);
        foundation.addInteractionRecord(record);
    }

    @After
    public void teardown() {
        foundationService.deleteAll();
    }

    @Test
    public void testGetInteractions() throws Exception {
        interactionService.create(record);
        InteractionRecord secondRecord = new InteractionRecord("Person Y", LocalDate.now(), "Email", foundation);
        interactionService.create(secondRecord);

        BayardTestUtilities.performEntityGetMultiple(Views.InteractionRecordList.class, INTERACTIONS_BASE_URL, mockMvc, record, secondRecord);
    }

    @Test
    public void testGetInteraction() throws Exception {
        interactionService.create(record);

        BayardTestUtilities.performEntityGetSingle(Views.InteractionRecordDetails.class, INTERACTIONS_BASE_URL + record.getId(), mockMvc, record);
    }

    @Test
    public void testPutInteractionDetails() throws Exception {
        interactionService.create(record);

        String newType = "A new interaction type";
        LocalDate newInteractionDate = LocalDate.of(1990, 1, 1);
        record.setInteractionType(newType);
        record.setDateOfInteraction(newInteractionDate);
        InteractionRecordDto dto = DtoTransformer.fromEntity(record);

        BayardTestUtilities.performEntityPut(INTERACTIONS_BASE_URL+record.getId(), dto, mockMvc);

        record = interactionService.findById(record.getId());
        assertEquals(newType, record.getInteractionType());
        assertEquals(newInteractionDate, record.getDateOfInteraction());

    }

    @Test
    public void testDeleteInteraction() throws Exception {
        interactionService.create(record);
        record = interactionService.findById(record.getId());
        assertNotNull(record);

        BayardTestUtilities.performEntityDelete(INTERACTIONS_BASE_URL + record.getId(), mockMvc);

        record = interactionService.findById(record.getId());
        assertNull(record);

        foundation = foundationService.findById(foundation.getId());
        assertTrue(foundation.getInteractionRecords().isEmpty());
    }


}
