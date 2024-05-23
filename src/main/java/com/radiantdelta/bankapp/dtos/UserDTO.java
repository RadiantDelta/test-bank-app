package com.radiantdelta.bankapp.dtos;
import com.radiantdelta.bankapp.entities.Email;
import com.radiantdelta.bankapp.entities.Phone;
import com.radiantdelta.bankapp.entities.User;

import java.util.Date;
import java.util.List;

public class UserDTO {
    private String fio;
    private List<Phone> phones;
    private List<Email> emails;

    private Date dob;

    private int amount;

    public UserDTO(String fio, List<Phone> phones, List<Email> emails, Date dob, int amount) {
        this.fio = fio;
        this.phones = phones;
        this.emails = emails;
        this.dob = dob;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }




    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    public static UserDTO from(User u) {
        return new UserDTO(u.getFio(), u.getPhoneList(), u.getEmailList(), u.getDob(), u.getAmount());
    }
}
