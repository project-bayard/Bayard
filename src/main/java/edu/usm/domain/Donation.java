package edu.usm.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDate;

@Entity(name = "donation")
public class Donation extends BasicEntity implements Serializable {

    @Column
    private int amount;
    @Column
    private String type;
    @Column
    private LocalDate date;
    @Column
    private String comment;

    public Donation(String id) {
        setId(id);
    }

    public Donation() {
        super();
    }

    @ManyToOne
    private DonorInfo donor;

    public DonorInfo getDonorInfo() {
        return donor;
    }

    public void setDonorInfo(DonorInfo donorInfo) {
        this.donor = donorInfo;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }




}