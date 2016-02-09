package edu.usm.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by andrew on 1/24/16.
 */
@Entity(name = "foundation")
public class Foundation extends BasicEntity implements Serializable{

    @Column
    @NotNull
    private String name;

    @Column
    private String address;

    @Column
    private String website;

    @Column
    private String phoneNumber;

    @Column
    private String primaryContactName;

    @Column
    private String primaryContactTitle;

    @Column
    private String primaryContactPhone;

    @Column
    private String primaryContactEmail;

    @Column
    private String secondaryContactName;

    @Column
    private String secondaryContactTitle;

    @Column
    private String secondaryContactPhone;

    @Column
    private String secondaryContactEmail;

    @Column
    private boolean currentGrantor;

    @OneToMany(mappedBy = "foundation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<InteractionRecord> interactionRecords;

    @OneToMany(mappedBy = "foundation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Grant> grants;

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

    public Set<InteractionRecord> getInteractionRecords() {
        if (null == interactionRecords) {
            interactionRecords = new HashSet<>();
        }
        return interactionRecords;
    }

    public void setInteractionRecords(Set<InteractionRecord> interactionRecords) {
        this.interactionRecords = interactionRecords;
    }

    public void addInteractionRecord(InteractionRecord record) {
        this.getInteractionRecords().add(record);
    }

    public Set<Grant> getGrants() {
        if (null == grants) {
            grants = new HashSet<>();
        }
        return grants;
    }

    public void setGrants(Set<Grant> grants) {
        this.grants = grants;
    }

    public void addGrant(Grant grant) {
        this.getGrants().add(grant);
    }
}
