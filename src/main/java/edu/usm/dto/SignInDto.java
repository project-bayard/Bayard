package edu.usm.dto;

import java.io.Serializable;

/**
 * Created by scottkimball on 2/6/16.
 */

public class SignInDto implements Serializable {
    private String firstName;
    private String lastName;
    private String nickName;
    private String email;
    private String phoneNumber;

    public SignInDto(String firstName, String lastName, String nickName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickName = nickName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public SignInDto() {
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
