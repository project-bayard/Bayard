package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Encapsulates information about donations and sustainer periods for a particular Contact.
 */
@Entity(name = "donor_info")
public class DonorInfo extends BasicEntity implements Serializable {

    @Column
    private boolean currentSustainer;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name="donor_info_id")
    private Set<Donation> donations;

    @OneToMany(mappedBy = "donorInfo", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @SortNatural
    private SortedSet<SustainerPeriod> sustainerPeriods = new TreeSet<>();

    public DonorInfo() {
        super();
    }

    /**
     * @return true if the contact is currently participating in a monthly/yearly contribution schedule
     */
    public boolean isCurrentSustainer() {
        return currentSustainer;
    }

    public void setCurrentSustainer(boolean isSustainer) {
        this.currentSustainer = isSustainer;
    }

    /**
     * @return the set of Donations for this particular Contact
     */
    public Set<Donation> getDonations() {
        return donations;
    }

    public void addDonation(Donation donation) {
        if (null == this.donations) {
            this.donations = new HashSet<>();
        }
        this.donations.add(donation);
    }

    public void setDonations(Set<Donation> donations) {
        this.donations = donations;
    }

    /**
     * @return the set of SustainerPeriods this Contact has committed to
     */
    public Set<SustainerPeriod> getSustainerPeriods() {
        return sustainerPeriods;
    }

    public void setSustainerPeriods(SortedSet<SustainerPeriod> sustainerPeriods) {
        this.sustainerPeriods.clear();
        this.sustainerPeriods.addAll(sustainerPeriods);
    }

    public void addSustainerPeriod(SustainerPeriod sustainerPeriod) {
        this.sustainerPeriods.add(sustainerPeriod);
    }
}
