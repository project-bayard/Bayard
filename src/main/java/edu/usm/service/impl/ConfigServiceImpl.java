package edu.usm.service.impl;

import edu.usm.domain.BayardConfig;
import edu.usm.repository.ConfigDao;
import edu.usm.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.*;
import java.util.Set;

/**
 * Created by andrew on 1/27/16.
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigDao configDao;

    @Value("${bayard.implementation.name}")
    private String implementationName;

    @Value("${version}")
    private String version;

    @Value("${bayard.implementation.largeLogoFilePath}")
    private String largeLogoFilePath;

    @Value("${bayard.implementation.faviconFilePath}")
    private String faviconFilePath;

    @Value("${bayard.implementation.enableDevelopmentFeatures}")
    private String developmentEnabled;

    @Value("${bayard.implementation.startupMode}")
    private String startupMode;

    @Value("${bayard.implementation.startup.demographic_categories}")
    private String[] demographicCategories;

    @Override
    public BayardConfig getImplementationConfig() {
        Set<BayardConfig> config = configDao.findAll();
        return config.size() == 0 ? null : config.iterator().next();
    }

    @Override
    public BayardConfig getStartupMode() {
        BayardConfig config = new BayardConfig();
        config.setStartupMode(getImplementationConfig().isStartupMode());
        return config;
    }

    @Override
    public String[] getStartupDemographicCategories() {
        return demographicCategories;
    }

    @Override
    public void disableStartupMode(){
        BayardConfig config = getImplementationConfig();
        config.setStartupMode(false);
        updateConfig(config);
    }

    /**
     * Creates a BayardConfig from config.properties file only if no config already exists
     * @throws IOException
     */
    @Override
    public void persistStartupConfig(){
        if (null == getImplementationConfig()) {
            BayardConfig bayardConfig = new BayardConfig();
            bayardConfig.setStartupMode(Boolean.valueOf(startupMode));
            bayardConfig.setDevelopmentEnabled(Boolean.valueOf(developmentEnabled));
            bayardConfig.setFaviconFilePath(faviconFilePath);
            bayardConfig.setImplementationName(implementationName);
            bayardConfig.setLargeLogoFilePath(largeLogoFilePath);
            bayardConfig.setVersion(version);
            updateConfig(bayardConfig);
        }
    }

    @Override
    @Transactional
    public void updateConfig(BayardConfig bayardConfig){

        BayardConfig existingConfig = null;

        if (configDao.findAll().size() != 0) {
            existingConfig = getImplementationConfig();
        }

        if (null == existingConfig) {
            existingConfig = bayardConfig;
        } else {
            existingConfig.setStartupMode(bayardConfig.isStartupMode());
            existingConfig.setDevelopmentEnabled(bayardConfig.isDevelopmentEnabled());
            existingConfig.setLargeLogoFilePath(bayardConfig.getLargeLogoFilePath());
            existingConfig.setFaviconFilePath(bayardConfig.getFaviconFilePath());
            existingConfig.setImplementationName(bayardConfig.getImplementationName());
            existingConfig.setVersion(bayardConfig.getVersion());
        }
        configDao.save(existingConfig);
    }

}
