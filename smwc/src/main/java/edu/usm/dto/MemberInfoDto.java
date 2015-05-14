package edu.usm.dto;

import edu.usm.domain.MemberInfo;

import java.io.Serializable;

/**
 * Created by scottkimball on 5/9/15.
 */
public class MemberInfoDto extends BasicEntityDto implements Serializable {

    private String id;
    private boolean paidDues;
    private boolean signedAgreement;
    private int status;

    public MemberInfo convertToMemberInfo () {
        MemberInfo memberInfo;
        if (getId() == null) {
            memberInfo = new MemberInfo();

        } else {
            memberInfo = new MemberInfo(getId());
            memberInfo.setCreated(getCreated());
            memberInfo.setLastModified(getLastModified());
        }
        memberInfo.setId(this.getId());
        memberInfo.setPaidDues(this.hasPaidDues());
        memberInfo.setSignedAgreement(this.hasSignedAgreement());
        memberInfo.setStatus(this.getStatus());
        return memberInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
