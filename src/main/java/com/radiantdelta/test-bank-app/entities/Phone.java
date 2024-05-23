package com.m1guelsb.springauth.entities;

import jakarta.persistence.*;


@Table()
@Entity(name = "phones")
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @ManyToOne()
    @JoinColumn(name = "user_id",
            referencedColumnName = "user_id")
    private com.m1guelsb.springauth.entities.User user;

    @Column(name = "phone")
    private String phone;

    public Phone() {}
    public Phone(com.m1guelsb.springauth.entities.User user, String phone) {
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
