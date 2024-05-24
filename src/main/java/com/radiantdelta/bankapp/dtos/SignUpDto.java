package com.radiantdelta.bankapp.dtos;

import com.radiantdelta.bankapp.entities.Email;
import com.radiantdelta.bankapp.entities.Phone;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.List;

@Validated
public class SignUpDto{
    @NotEmpty(message = "Input login cannot be empty.")
    private String login;
    @NotEmpty(message = "Input password cannot be empty.")
    private String password;
    @Size(min = 1, message = "List of Phone must have at least one element")
    @NotEmpty(message = "Input phone list cannot be empty.")
    private List<Phone> phoneList;
    @Size(min = 1, message = "List of Email must have at least one element")
    @NotEmpty(message = "Input email list cannot be empty.")
    private List<Email> emailList;
    @NotNull(message = "Input amount cannot be null.")
    private float amount;

    @NotNull(message = "Input Date cannot be null.")
    private Date dob;
    @NotEmpty(message = "Input FIO cannot be empty.")
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

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public SignUpDto(String login, String password, List<Phone> phoneList, List<Email> emailList, float amount, Date dob, String fio) {
        this.login = login;
        this.password = password;
        this.phoneList = phoneList;
        this.emailList = emailList;
        this.amount = amount;
        this.dob = dob;
        this.fio = fio;
    }
}
