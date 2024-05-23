package com.radiantdelta.bankapp.dtos;

import com.radiantdelta.bankapp.entities.Email;
import com.radiantdelta.bankapp.entities.Phone;

import java.util.Date;
import java.util.List;

public class SignUpDto{
    private String login;
    private String password;
    private List<Phone> phoneList;
    private List<Email> emailList;
    private int amount;

    private Date dob;
    private String fio;

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Phone> getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(List<Phone> phoneList) {
        this.phoneList = phoneList;
    }

    public List<Email> getEmailList() {
        return emailList;
    }

    public void setEmailList(List<Email> emailList) {
        this.emailList = emailList;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
