package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.config.DateFormatConfig;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents an interaction at a specific point in time between a Contact from the organization running Bayard and
 * another Contact.
 */
@Entity(name = "encounter")
public class Encounter extends BasicEntity implements Serializable, Comparable<Encounter> {

    public static final int DEFAULT_ASSESSMENT = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "contact_id")
    @JsonView(Views.ContactEncounterDetails.class)
    private Contact contact;

    @Column
    @NotNull
    @JsonView({Views.ContactDetails.class, Views.ContactEncounterDetails.class})
    private String encounterDate;

    @Column
    @JsonView({Views.ContactDetails.class, Views.ContactEncounterDetails.class})
    private String notes;

    @ManyToOne(fetch = FetchType.EAGER )
    @JoinColumn(name = "initiator_id")
    @JsonView(Views.ContactEncounterDetails.class)
    private Contact initiator;

    @Column
    @JsonView({Views.ContactDetails.class, Views.ContactEncounterDetails.class})
    private int assessment;

    @Column
    @NotNull
    @JsonView({Views.ContactDetails.class, Views.ContactEncounterDetails.class})
    private String type;

    @Column
    @JsonView({Views.ContactDetails.class, Views.ContactEncounterDetails.class})
    private boolean requiresFollowUp;

    @Override
    public int compareTo(Encounter o2) {
        if (this.getEncounterDate() == null && o2.getEncounterDate() == null) {
            return 0;
        } else if (this.getEncounterDate() == null) {
            return 1;
        } else if (o2.getEncounterDate() == null) {
            return -1;
        } else {
            DateTimeFormatter formatter = DateFormatConfig.getDateTimeFormatter();
            LocalDate thisDate = LocalDate.parse(this.getEncounterDate(), formatter);
            LocalDate otherDate = LocalDate.parse(o2.getEncounterDate(), formatter);

            return otherDate.compareTo(thisDate);
        }
    }

    public Encounter() {
        super();
    }

    /**
     * @param contact the subject in the Encounter
     * @param initiator the initiator of the Encounter
     * @param date the date of the Encounter
     * @param type the type of the Encounter
     */
    public Encounter(Contact contact, Contact initiator, String date, String type) {
        super();
        this.contact = contact;
        this.initiator = initiator;
        this.encounterDate = date;
        this.type = type;
    }

    /**
     * @return the subject in the Encounter
     */
    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    /**
     * @return the date of the Encounter
     */
    public String getEncounterDate() {
        return encounterDate;
    }

    public void setEncounterDate(String encounterDate) {
        this.encounterDate = encounterDate;
    }

    /**
     * @return the notes pertaining to Encounter
     */
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * @return the initiator of the Encounter
     */
    public Contact getInitiator() {
        return initiator;
    }

    public void setInitiator(Contact initiator) {
        this.initiator = initiator;
    }

    /**
     * @return the assessment rating given to the Contact at this particular Encounter
     */
    public int getAssessment() {
        return assessment;
    }

    public void setAssessment(int assessment) {
        this.assessment = assessment;
    }

    /**
     * @return the type of Encounter
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return true if the Encounter requires a follow-up interaction by someone in the organization
     */
    public boolean requiresFollowUp() {
        return requiresFollowUp;
    }

    public void setRequiresFollowUp(boolean requiresFollowUp) {
        this.requiresFollowUp = requiresFollowUp;
    }
}
