package edu.usm.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

public class DonorInfo {

    //TODO: decide how to represent sustaining donorships vs a collection of one-time donations
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToOne(mappedBy = "donorInfo")
    private Contact contact;
    @Column
    private boolean isSustainer;
    @Column
    private LocalDateTime date;
    @OneToMany(mappedBy="donorInfo")
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
        return isSustainer;
    }

    public void setSustainer(boolean isSustainer) {
        this.isSustainer = isSustainer;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<Donation> getDonations() {
        return donations;
    }

    public void setDonations(List<Donation> donations) {
        this.donations = donations;
    }
}
