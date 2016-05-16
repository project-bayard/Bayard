package edu.usm.it.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.BayardConfig;
import edu.usm.repository.ConfigDao;
import edu.usm.service.ConfigService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by andrew on 1/27/16.
 */
public class ConfigControllerTest extends WebAppConfigurationAware {

    @Autowired
    private ConfigService configService;

    @Autowired
    private ConfigDao dao;

    @Before
    public void setup() throws Exception{
        configService.persistStartupConfig();
    }

    @After
    public void teardown() {
        dao.deleteAll();
    }

    @Test
    public void testGetImplementationConfig() throws Exception{

        mockMvc.perform(get("/config").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$implementationName", is(configService.getImplementationConfig().getImplementationName())))
                .andExpect(jsonPath("$version", is(configService.getImplementationConfig().getVersion())));

    }

    @Test
    public void testDisableStartupMode() throws Exception {

        BayardConfig config = configService.getImplementationConfig();
        config.setStartupMode(true);
        configService.updateConfig(config);
        config = configService.getImplementationConfig();
        assertTrue(config.isStartupMode());

        mockMvc.perform(post("/config/startup/disable").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        config = configService.getImplementationConfig();
        assertFalse(config.isStartupMode());

    }

    @Test
    public void updateImplementationConfig() throws Exception {
        BayardConfig config = configService.getImplementationConfig();
        String newVersion = "123111";
        String newImplementationName = "New Impl";
        String newLogoFilepath = "New Logo Filepath";
        boolean developmentEnabled = false;
        config.setVersion(newVersion);
        config.setImplementationName(newImplementationName);
        config.setLargeLogoFilePath(newLogoFilepath);
        config.setDevelopmentEnabled(developmentEnabled);

        String json = new ObjectMapper().writeValueAsString(config);

        mockMvc.perform(put("/config")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        config = configService.getImplementationConfig();
        assertEquals(newVersion, config.getVersion());
        assertEquals(newImplementationName, config.getImplementationName());
        assertEquals(newLogoFilepath, config.getLargeLogoFilePath());
        assertEquals(developmentEnabled, config.isDevelopmentEnabled());

    }

}
