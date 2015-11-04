package edu.usm.dto;

import java.io.Serializable;

/**
 * Created by andrew on 11/3/15.
 */
public class PasswordChangeDto implements Serializable {

    private String currentPassword;
    private String newPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
