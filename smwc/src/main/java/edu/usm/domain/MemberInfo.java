package edu.usm.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * Created by justin on 2/19/15.
 */

@Entity(name = "member_info")
public class MemberInfo extends BasicEntity  implements Serializable {

    public static int STATUS_GOOD = 1;
    public static int STATUS_BAD = 2;
    public static int STATUS_OTHER = 3;



    @Column
    private boolean paidDues;

    @Column
    private boolean signedAgreement;

    @Column
    private int status;

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
