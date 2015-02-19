package edu.usm.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private int amount;
    @Column
    private String type;
    @Column
    private LocalDateTime date;
    @Column
    private String comment;
    @Column
    private boolean isIrsLetterSent;
    @Column
    private boolean isThankYouLetterSent;
    @ManyToOne(fetch = FetchType.LAZY)
    private DonorInfo donorInfo;

    public DonorInfo getDonorInfo() {
        return donorInfo;
    }

    public void setDonorInfo(DonorInfo donorInfo) {
        this.donorInfo = donorInfo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isIrsLetterSent() {
        return isIrsLetterSent;
    }

    public void setIrsLetterSent(boolean isIrsLetterSent) {
        this.isIrsLetterSent = isIrsLetterSent;
    }

    public boolean isThankYouLetterSent() {
        return isThankYouLetterSent;
    }

    public void setThankYouLetterSent(boolean isThankYouLetterSent) {
        this.isThankYouLetterSent = isThankYouLetterSent;
    }
}