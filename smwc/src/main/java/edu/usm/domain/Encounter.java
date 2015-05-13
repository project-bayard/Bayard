package edu.usm.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;


@Entity(name = "encounter")
public class Encounter extends BasicEntity implements Serializable {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id")
    private Contact contact;

    @Column
    private LocalDate date;

    @Column
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.REFRESH)
    @JoinColumn(name = "initiator_id")
    private Contact initiator;

    @Column
    private int assessment;

    @Column
    private EncounterType type;

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public EncounterType getType() {
        return type;
    }

    public void setType(EncounterType type) {
        this.type = type;
    }


}
