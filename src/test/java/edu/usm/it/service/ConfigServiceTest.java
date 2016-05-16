package edu.usm.it.service;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.BayardConfig;
import edu.usm.repository.ConfigDao;
import edu.usm.service.ConfigService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Created by andrew on 4/26/16.
 */
public class ConfigServiceTest extends WebAppConfigurationAware {

    private static String DEFAULT_FAVICON_FILEPATH = "resources/images/default-favicon.ico";

    @Autowired
    ConfigService configService;

    @Autowired
    ConfigDao configDao;

    @Before
    public void setup() throws Exception{
        configService.persistStartupConfig();
    }

    @After
    public void teardown() {
        configDao.deleteAll();
    }

    @Test
    public void testGetConfig() {
        BayardConfig config = configService.getImplementationConfig();
        assertNotNull(config);
        assertEquals(DEFAULT_FAVICON_FILEPATH, config.getFaviconFilePath());
    }

    @Test
    public void testDisableStartupMode() throws Exception{
        configService.disableStartupMode();
        BayardConfig config = configService.getImplementationConfig();
        assertFalse(config.isStartupMode());
    }

    @Test
    public void testUpdateConfig() throws Exception{

        String newImplementationName = "New Implementation Name";
        String newFaviconFilepath = "New Favicon Filepath";

        BayardConfig newConfig = configService.getImplementationConfig();
        newConfig.setImplementationName(newImplementationName);
        newConfig.setFaviconFilePath(newFaviconFilepath);
        configService.updateConfig(newConfig);

        newConfig = configService.getImplementationConfig();
        assertEquals(newImplementationName, newConfig.getImplementationName());
        assertEquals(newFaviconFilepath, newConfig.getFaviconFilePath());
    }

}
