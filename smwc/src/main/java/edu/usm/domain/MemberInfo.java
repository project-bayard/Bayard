package edu.usm.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by justin on 2/19/15.
 */

@Entity
public class MemberInfo  implements Serializable {

    public static int STATUS_GOOD = 1;
    public static int STATUS_BAD = 2;
    public static int STATUS_OTHER = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne(mappedBy = "memberInfo")
    private Contact contact;

    @Column
    private boolean paidDues;

    @Column
    private boolean signedAgreement;

    @Column
    private int status;

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public boolean hasPaidDues() {
        return paidDues;
    }

    public void setPaidDues(boolean paidDues) {
        this.paidDues = paidDues;
    }

    public boolean hasSignedAgreement() {
        return signedAgreement;
    }

    public void setSignedAgreement(boolean signedAgreement) {
        this.signedAgreement = signedAgreement;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
