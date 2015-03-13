package edu.usm.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by justin on 2/19/15.
 */
@Entity
public class Encounter  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Contact contact;

    @Column
    private LocalDateTime date;

    @Column
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    private Contact initiator;

    @Column
    private int assessment;

    @Column
    private String type;

    @Column
    private boolean requiresFollowup;

    @OneToOne(fetch=FetchType.LAZY)
    private Form form;

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRequiresFollowup() {
        return requiresFollowup;
    }

    public void setRequiresFollowup(boolean requiresFollowup) {
        this.requiresFollowup = requiresFollowup;
    }
}
