package edu.usm.it.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.EncounterType;
import edu.usm.dto.Response;
import edu.usm.service.EncounterTypeService;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by scottkimball on 8/17/15.
 */
public class EncounterTypeControllerTest extends WebAppConfigurationAware {

    @Autowired
    EncounterTypeService encounterTypeService;

    @After
    public void teardown() {
        encounterTypeService.deleteAll();
    }


    @Test
    public void testCreateEncounterType() throws Exception {
        EncounterType encounterType = new EncounterType();
        String name = "name";
        encounterType.setName(name);

        String json = new ObjectMapper().writeValueAsString(encounterType);

        mockMvc.perform(post("/encounterTypes").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is(Response.SUCCESS)));

        EncounterType fromDb = encounterTypeService.findByName(name);
        assertNotNull(fromDb);
        assertEquals(fromDb.getName(),name);
    }

    @Test
    public void testGetAllEncounterTypes() throws Exception {
        EncounterType encounterType = new EncounterType();
        String name = "name";
        encounterType.setName(name);

        String id = encounterTypeService.create(encounterType);

        mockMvc.perform(get("/encounterTypes").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].name", is(name)))
                .andExpect(jsonPath("$.[0].id", is(id)));
    }

    @Test
    public void testDeleteEncounterType() throws Exception {
        EncounterType encounterType = new EncounterType();
        String name = "name";
        encounterType.setName(name);

        String id = encounterTypeService.create(encounterType);
        Set<EncounterType> encounterTypes = encounterTypeService.findAll();
        assertEquals(encounterTypes.size(), 1);

        mockMvc.perform(delete("/encounterTypes/" + id))
                .andExpect(status().isOk());

        encounterTypes = encounterTypeService.findAll();
        assertEquals(encounterTypes.size(), 0);
    }



 }
