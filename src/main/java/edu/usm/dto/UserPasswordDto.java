package edu.usm.dto;

import java.io.Serializable;

/**
 * Created by andrew on 11/3/15.
 */
public class UserPasswordDto implements Serializable {

    private String currentpassword;
    private String newPassword;

    public String getCurrentpassword() {
        return currentpassword;
    }

    public void setCurrentpassword(String currentpassword) {
        this.currentpassword = currentpassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
