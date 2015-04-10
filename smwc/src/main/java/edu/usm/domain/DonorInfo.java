package edu.usm.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
public class DonorInfo  implements Serializable {

    //TODO: decide how to represent sustaining donorships vs a collection of one-time donations
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne(mappedBy = "donorInfo")
    private Contact contact;

    @Column
    private boolean sustainer;

    @Column
    private LocalDate date;

    @Column
    private boolean irsLetterSent;
    @Column
    private boolean thankYouLetterSent;

    @OneToMany(mappedBy="donor", fetch = FetchType.EAGER)
    private List<Donation> donations;

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

    public boolean isSustainer() {
        return sustainer;
    }

    public void setSustainer(boolean isSustainer) {
        this.sustainer = isSustainer;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Donation> getDonations() {
        return donations;
    }

    public void setDonations(List<Donation> donations) {
        this.donations = donations;
    }

    public boolean isIrsLetterSent() {
        return irsLetterSent;
    }

    public void setIrsLetterSent(boolean irsLetterSent) {
        this.irsLetterSent = irsLetterSent;
    }

    public boolean isThankYouLetterSent() {
        return thankYouLetterSent;
    }

    public void setThankYouLetterSent(boolean thankYouLetterSent) {
        this.thankYouLetterSent = thankYouLetterSent;
    }
}
