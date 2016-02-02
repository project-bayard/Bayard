package edu.usm.it.controller;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.service.ConfigService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by andrew on 1/27/16.
 */
public class ConfigControllerTest extends WebAppConfigurationAware {

    @Autowired
    private ConfigService configService;

    @Test
    public void testGetImplementationConfig() throws Exception{

        mockMvc.perform(get("/config").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$implementationName", is(configService.getImplementationConfig().getImplementationName())))
                .andExpect(jsonPath("$version", is(configService.getImplementationConfig().getVersion())));

    }

}
