package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * Encapsulates membership information for a particular Contact.
 */
@Entity(name = "member_info")
public class MemberInfo extends BasicEntity  implements Serializable {

    public static int STATUS_GOOD = 1;
    public static int STATUS_BAD = 2;
    public static int STATUS_OTHER = 3;

    @Column
    @JsonView(Views.MemberInfo.class)
    private boolean paidDues;

    @Column
    @JsonView(Views.MemberInfo.class)
    private boolean signedAgreement;

    @Column
    @JsonView(Views.MemberInfo.class)
    private int status;

    @Column
    @JsonView(Views.MemberInfo.class)
    private boolean attendedOrientation;

    public MemberInfo() {
        super();
    }

    public boolean hasAttendedOrientation() {
        return attendedOrientation;
    }

    public void setAttendedOrientation(boolean attendedOrientation) {
        this.attendedOrientation = attendedOrientation;
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

    /**
     * @return the current membership status code of this Contact
     */
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
