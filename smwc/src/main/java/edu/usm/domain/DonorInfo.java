package edu.usm.domain;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity(name = "donor_info")
@SQLDelete(sql="UPDATE donor_info SET deleted = '1' WHERE id = ?")
@Where(clause="deleted <> '1'")
public class DonorInfo extends BasicEntity implements Serializable {


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
