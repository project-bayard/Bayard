package edu.usm.it.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.DemographicCategory;
import edu.usm.domain.DemographicOption;
import edu.usm.service.DemographicCategoryService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by andrew on 10/16/15.
 */
public class DemographicCategoryControllerTest extends WebAppConfigurationAware {

    @Autowired
    private DemographicCategoryService service;

    private DemographicCategory demographicCategory;
    private Set<DemographicOption> options;

    @Before
    public void setup() {
        demographicCategory = new DemographicCategory();
        demographicCategory.setName("New Category");

        DemographicOption option1 = new DemographicOption();
        option1.setName("First Option");

        options = new HashSet<>();
        options.add(option1);

        demographicCategory.setOptions(options);
    }

    @After
    public void teardown() {
        service.deleteAll();
    }

    @Test
    public void testGetAll() throws Exception {
        service.create(demographicCategory);

        mockMvc.perform(get("/demographics").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(demographicCategory.getId())));

    }

    @Test
    public void testGetOne() throws Exception{

        service.create(demographicCategory);

        mockMvc.perform(get("/demographics/"+demographicCategory.getName()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(demographicCategory.getId())));

    }

    @Test
    public void testCreateOption() throws Exception{

        DemographicOption option2 = new DemographicOption();
        option2.setName("asdfasdfasdf");

        demographicCategory.getOptions().add(option2);

        service.create(demographicCategory);

        DemographicCategory fromDb = service.findByName(demographicCategory.getName());
        assertEquals(2, fromDb.getOptions().size());

        DemographicOption newOption = new DemographicOption();
        newOption.setName("A New Option");

        String json = new ObjectMapper().writeValueAsString(newOption);

        mockMvc.perform(MockMvcRequestBuilders.post("/demographics/" + demographicCategory.getName() + "/options")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        fromDb = service.findByName(demographicCategory.getName());
        assertNotNull(fromDb);
        assertEquals(3, fromDb.getOptions().size());

    }

}
