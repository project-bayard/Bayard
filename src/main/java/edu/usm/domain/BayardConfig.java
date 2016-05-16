package edu.usm.domain;

import javax.persistence.Entity;

/**
 * Created by andrew on 4/25/16.
 */
@Entity
public class BayardConfig extends BasicEntity{

    private String implementationName;

    private String version;

    private String largeLogoFilePath;

    private String faviconFilePath;

    private boolean developmentEnabled;

    private boolean startupMode;

    public String getImplementationName() {
        return implementationName;
    }

    public void setImplementationName(String implementationName) {
        this.implementationName = implementationName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLargeLogoFilePath() {
        return largeLogoFilePath;
    }

    public void setLargeLogoFilePath(String largeLogoFilePath) {
        this.largeLogoFilePath = largeLogoFilePath;
    }

    public String getFaviconFilePath() {
        return faviconFilePath;
    }

    public void setFaviconFilePath(String faviconFilePath) {
        this.faviconFilePath = faviconFilePath;
    }

    public boolean isDevelopmentEnabled() {
        return developmentEnabled;
    }

    public void setDevelopmentEnabled(boolean developmentEnabled) {
        this.developmentEnabled = developmentEnabled;
    }

    public boolean isStartupMode() {
        return startupMode;
    }

    public void setStartupMode(boolean startupMode) {
        this.startupMode = startupMode;
    }
}
