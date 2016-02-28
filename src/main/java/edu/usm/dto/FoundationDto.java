package edu.usm.dto;

import java.io.Serializable;

/**
 * Created by andrew on 2/16/16.
 */
public class FoundationDto implements Serializable{

    private String name;

    private String address;

    private String website;

    private String phoneNumber;

    private String primaryContactName;

    private String primaryContactTitle;

    private String primaryContactPhone;

    private String primaryContactEmail;

    private String secondaryContactName;

    private String secondaryContactTitle;

    private String secondaryContactPhone;

    private String secondaryContactEmail;

    private boolean currentGrantor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPrimaryContactName() {
        return primaryContactName;
    }

    public void setPrimaryContactName(String primaryContactName) {
        this.primaryContactName = primaryContactName;
    }

    public String getPrimaryContactTitle() {
        return primaryContactTitle;
    }

    public void setPrimaryContactTitle(String primaryContactTitle) {
        this.primaryContactTitle = primaryContactTitle;
    }

    public String getPrimaryContactPhone() {
        return primaryContactPhone;
    }

    public void setPrimaryContactPhone(String primaryContactPhone) {
        this.primaryContactPhone = primaryContactPhone;
    }

    public String getPrimaryContactEmail() {
        return primaryContactEmail;
    }

    public void setPrimaryContactEmail(String primaryContactEmail) {
        this.primaryContactEmail = primaryContactEmail;
    }

    public String getSecondaryContactName() {
        return secondaryContactName;
    }

    public void setSecondaryContactName(String secondaryContactName) {
        this.secondaryContactName = secondaryContactName;
    }

    public String getSecondaryContactTitle() {
        return secondaryContactTitle;
    }

    public void setSecondaryContactTitle(String secondaryContactTitle) {
        this.secondaryContactTitle = secondaryContactTitle;
    }

    public String getSecondaryContactPhone() {
        return secondaryContactPhone;
    }

    public void setSecondaryContactPhone(String secondaryContactPhone) {
        this.secondaryContactPhone = secondaryContactPhone;
    }

    public String getSecondaryContactEmail() {
        return secondaryContactEmail;
    }

    public void setSecondaryContactEmail(String secondaryContactEmail) {
        this.secondaryContactEmail = secondaryContactEmail;
    }

    public boolean isCurrentGrantor() {
        return currentGrantor;
    }

    public void setCurrentGrantor(boolean currentGrantor) {
        this.currentGrantor = currentGrantor;
    }
}
