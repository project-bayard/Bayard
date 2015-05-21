package edu.usm.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;


@Entity(name = "encounter")
public class Encounter extends BasicEntity implements Serializable, Comparable<Encounter>{


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id")
    private Contact contact;

    @Column
    private String encounterDate;

    @Column
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.REFRESH)
    @JoinColumn(name = "initiator_id")
    private Contact initiator;

    @Column
    private int assessment;

    @Column
    private String type;

    @OneToOne(fetch=FetchType.LAZY)
    private Form form;

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

    @Override
    /*
     * by most recent date
     */
    public int compareTo(Encounter o) {
        if (this.getEncounterDate() == null && o.getEncounterDate() == null) {
            return 0;
        } else if (this.getEncounterDate() == null) {
            return 1;
        } else if (o.getEncounterDate() == null) {
            return -1;
        } else {
            LocalDate thisDate = LocalDate.parse(this.getEncounterDate());
            LocalDate otherDate = LocalDate.parse(o.getEncounterDate());

            return otherDate.compareTo(thisDate);
        }
    }


}
