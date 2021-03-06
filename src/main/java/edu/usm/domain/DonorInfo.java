package edu.usm.domain;

import javax.persistence.*;
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
    private Set<SustainerPeriod> sustainerPeriods;

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
        if (null == sustainerPeriods) {
            sustainerPeriods = new HashSet<>();
        }
        return sustainerPeriods;
    }

    public void setSustainerPeriods(Set<SustainerPeriod> sustainerPeriods) {
        this.sustainerPeriods.clear();
        this.sustainerPeriods.addAll(sustainerPeriods);
    }

    public void addSustainerPeriod(SustainerPeriod sustainerPeriod) {
        this.getSustainerPeriods().add(sustainerPeriod);
    }
}
