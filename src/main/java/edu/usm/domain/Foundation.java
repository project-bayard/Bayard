 package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents any provider of Grants known to the organization running Bayard.
 */
@Entity(name = "foundation")
public class Foundation extends BasicEntity implements Serializable{

    @Column(unique = true)
    @NotNull
    @JsonView({Views.FoundationDetails.class,
            Views.FoundationList.class,
            Views.GrantDetails.class,
            Views.GrantList.class,
            Views.InteractionRecordList.class,
            Views.InteractionRecordDetails.class})
    private String name;

    @Column
    @JsonView({Views.FoundationDetails.class, Views.FoundationList.class})
    private String address;

    @Column
    @JsonView({Views.FoundationDetails.class})
    private String website;

    @Column
    @JsonView({Views.FoundationDetails.class, Views.FoundationList.class})
    private String phoneNumber;

    @Column
    @JsonView({Views.FoundationDetails.class, Views.FoundationList.class})
    private String primaryContactName;

    @Column
    @JsonView({Views.FoundationDetails.class})
    private String primaryContactTitle;

    @Column
    @JsonView({Views.FoundationDetails.class})
    private String primaryContactPhone;

    @Column
    @JsonView({Views.FoundationDetails.class})
    private String primaryContactEmail;

    @Column
    @JsonView({Views.FoundationDetails.class})
    private String secondaryContactName;

    @Column
    @JsonView({Views.FoundationDetails.class})
    private String secondaryContactTitle;

    @Column
    @JsonView({Views.FoundationDetails.class})
    private String secondaryContactPhone;

    @Column
    @JsonView({Views.FoundationDetails.class})
    private String secondaryContactEmail;

    @Column
    @JsonView({Views.FoundationDetails.class, Views.FoundationList.class})
    private boolean currentGrantor;

    @OneToMany(mappedBy = "foundation", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonView({Views.FoundationDetails.class})
    private Set<InteractionRecord> interactionRecords;

    @OneToMany(mappedBy = "foundation", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonView({Views.FoundationDetails.class})
    private Set<Grant> grants;

    public Foundation() {
        super();
    }

    /**
     * @param foundationName the name of the Foundation
     */
    public Foundation(String foundationName) {
        super();
        this.name = foundationName;
    }

    /**
     * @return the name of the Foundation
     */
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

    /**
     * @return true if this Foundation is currently providing a Grant
     */
    public boolean isCurrentGrantor() {
        return currentGrantor;
    }

    public void setCurrentGrantor(boolean currentGrantor) {
        this.currentGrantor = currentGrantor;
    }

    /**
     * @return the InteractionRecords associated with this Foundation
     */
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

    /**
     * @return the Grants associated with this Foundation
     */
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
