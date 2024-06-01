package com.radiantdelta.bankapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;


@Validated
@Entity(name = "emails")
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne()
    @JoinColumn(name = "user_id",
            referencedColumnName = "user_id")
    private User user;


    @NotEmpty(message = "Input email cannot be empty.")
    @Column(name = "email")
    private String emailstr;

    public Email() {}
    public Email(User user, String emailstr) {
        this.user =user;
        this.emailstr = emailstr;

    }


    public void setId(int id) { this.id = id; }

    public String getEmail() { return emailstr; }
    public void setEmail(String emailstr) { this.emailstr = emailstr; }


    public int getId() {
        return id;
    }
}
