package edu.usm.it.controller;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Foundation;
import edu.usm.domain.InteractionRecord;
import edu.usm.domain.InteractionRecordType;
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
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    InteractionRecordType type;

    @Before
    public void setup() throws ConstraintViolation {
        foundation = new Foundation("Test foundation");
        foundationService.create(foundation);

        type = new InteractionRecordType("Test interaction record type");
        interactionService.createInteractionRecordType(type);

        record = new InteractionRecord("Person X", LocalDate.now(), type, foundation);
        record.setNotes("Notes from the interaction");
        record.setRequiresFollowUp(true);
        foundation.addInteractionRecord(record);
    }

    @After
    public void teardown() {
        foundationService.deleteAll();
        interactionService.deleteAllInteractionRecordTypes();
        interactionService.deleteAll();
    }

    @Test
    public void testGetInteractions() throws Exception {
        interactionService.create(record);
        InteractionRecord secondRecord = new InteractionRecord("Person Y", LocalDate.now(), type, foundation);
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

        InteractionRecordType newType = new InteractionRecordType("A Different Type");
        interactionService.createInteractionRecordType(newType);

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

    @Test
    public void testGetAllInteractionTypes() throws Exception {
        mockMvc.perform(get(INTERACTIONS_BASE_URL + "interactiontypes")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(type.getId())))
                .andExpect(jsonPath("$.[0].name", is(type.getName())));
    }


    @Test
    public void testCreateInteractionType() throws Exception {
        InteractionRecordType newInteractionRecordType = new InteractionRecordType("Another interaction type");
        BayardTestUtilities.performEntityPost(INTERACTIONS_BASE_URL+"interactiontypes", newInteractionRecordType, mockMvc);

        Set<InteractionRecordType> recordTypes = interactionService.findAllInteractionRecordTypes();
        assertEquals(2, recordTypes.size());
    }


    @Test
    public void testDeleteInteractionType() throws Exception {
        BayardTestUtilities.performEntityDelete(INTERACTIONS_BASE_URL+"interactiontypes/"+type.getId(), mockMvc);
        type = interactionService.findInteractionRecordType(type.getId());
        assertNull(type);
    }


    @Test
    public void testChangeInteractionTypeName() throws Exception {
        type.setName("An updated type name");
        BayardTestUtilities.performEntityPut(INTERACTIONS_BASE_URL + "interactiontypes/" + type.getId(), type, mockMvc);
        InteractionRecordType fromDb = interactionService.findInteractionRecordType(type.getId());
        assertEquals(type.getName(), fromDb.getName());
    }


}
