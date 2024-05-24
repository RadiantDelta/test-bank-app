package com.radiantdelta.bankapp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;


@Validated
@Table()
@Entity(name = "phones")
public class Phone {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @ManyToOne()
    @JoinColumn(name = "user_id",
            referencedColumnName = "user_id")
    private User user;

    @NotEmpty(message = "Input phone cannot be empty.")
    @Column(name = "phone")
    private String phone;

    public Phone() {}
    public Phone(User user, String phone) {
        this.user = user;
        this.phone = phone;

    }


    public void setId(int id) { this.id = id; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }


    public int getId() {
        return id;
    }
}
