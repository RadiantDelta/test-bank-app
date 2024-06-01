package com.radiantdelta.bankapp.dtos;
import com.radiantdelta.bankapp.domain.Email;
import com.radiantdelta.bankapp.domain.Phone;
import com.radiantdelta.bankapp.domain.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.Date;
import java.util.List;

public class UserDTO {
    @NotEmpty(message = "Input fio cannot be empty.")
    private String fio;
    @NotEmpty(message = "Input Phone list cannot be empty.")
    private List< @Valid Phone> phones;
    @NotEmpty(message = "Input Email list cannot be empty.")
    private List<@Valid Email> emails;

    @NotEmpty(message = "Input date cannot be empty.")
    private Date dob;
    @NotEmpty(message = "Input  amount cannot be empty.")
    private float amount;

    public UserDTO(String fio, List<Phone> phones, List<Email> emails, Date dob, float amount) {
        this.fio = fio;
        this.phones = phones;
        this.emails = emails;
        this.dob = dob;
        this.amount = amount;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
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
