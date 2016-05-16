package edu.usm.service;

import edu.usm.domain.BayardConfig;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Created by andrew on 1/27/16.
 */
public interface ConfigService {

    /**
     * @return The BayardConfig for this implementation
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    BayardConfig getImplementationConfig();

    /**
     * @return Returns a BayardConfig that merely indicates whether or not startup mode is enabled.
     */
    BayardConfig getStartupMode();

    /**
     * @return the DemographicCategories defined in config.properties
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_SUPERUSER')")
    String[] getStartupDemographicCategories();

    @PreAuthorize(value = "hasAnyRole('ROLE_SUPERUSER')")
    void persistStartupConfig();

    @PreAuthorize(value = "hasAnyRole('ROLE_SUPERUSER')")
    void updateConfig(BayardConfig bayardConfig);

    @PreAuthorize(value = "hasAnyRole('ROLE_SUPERUSER')")
    void disableStartupMode();

}
