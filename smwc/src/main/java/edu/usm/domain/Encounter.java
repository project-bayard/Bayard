package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.config.DateFormatConfig;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Entity(name = "encounter")
public class Encounter extends BasicEntity implements Serializable, Comparable<Encounter> {

    public static final int DEFAULT_ASSESSMENT = 0;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "contact_id")
    @JsonView(Views.ContactEncounterDetails.class)
    private Contact contact;

    @Column
    @JsonView({Views.ContactDetails.class, Views.ContactEncounterDetails.class})
    private String encounterDate;

    @Column
    @JsonView({Views.ContactDetails.class, Views.ContactEncounterDetails.class})
    private String notes;

    @ManyToOne(fetch = FetchType.EAGER , cascade = CascadeType.REFRESH)
    @JoinColumn(name = "initiator_id")
    @JsonView(Views.ContactEncounterDetails.class)
    private Contact initiator;

    @Column
    @JsonView({Views.ContactDetails.class, Views.ContactEncounterDetails.class})
    private int assessment;

    @Column
    @JsonView({Views.ContactDetails.class, Views.ContactEncounterDetails.class})
    private String type;

    @OneToOne(fetch=FetchType.LAZY)
    private Form form;

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

    public Encounter (String id) {
        setId(id);
    }

    public Encounter() {
        super();
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getEncounterDate() {
        return encounterDate;
    }

    public void setEncounterDate(String encounterDate) {
        this.encounterDate = encounterDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Contact getInitiator() {
        return initiator;
    }

    public void setInitiator(Contact initiator) {
        this.initiator = initiator;
    }

    public int getAssessment() {
        return assessment;
    }

    public void setAssessment(int assessment) {
        this.assessment = assessment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
