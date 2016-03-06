package edu.usm.domain;

import java.io.Serializable;

/**
 * Created by andrew on 1/27/16.
 */
public class ConfigDto implements Serializable{

    private String implementationName;

    private String version;

    private String largeLogoFilePath;

    private String faviconFilePath;

    private boolean developmentEnabled;

    public boolean isDevelopmentEnabled() {
        return developmentEnabled;
    }

    public void setDevelopmentEnabled(boolean developmentEnabled) {
        this.developmentEnabled = developmentEnabled;
    }

    public String getFaviconFilePath() {
        return faviconFilePath;
    }

    public void setFaviconFilePath(String faviconFilePath) {
        this.faviconFilePath = faviconFilePath;
    }

    public String getLargeLogoFilePath() {
        return largeLogoFilePath;
    }

    public void setLargeLogoFilePath(String largeLogoFilePath) {
        this.largeLogoFilePath = largeLogoFilePath;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getImplementationName() {
        return implementationName;
    }

    public void setImplementationName(String implementationName) {
        this.implementationName = implementationName;
    }
}
