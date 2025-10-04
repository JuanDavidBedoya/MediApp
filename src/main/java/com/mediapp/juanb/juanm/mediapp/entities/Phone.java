package com.mediapp.juanb.juanm.mediapp.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "phones")
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_phone", updatable = false, nullable = false, unique = true)
    private UUID idPhone;

    @Column(name = "phone", nullable = false)
    private String phone;

    @OneToMany(mappedBy = "phone")
    private List<UserPhone> userPhones = new ArrayList<>();

    public Phone() {}

    public Phone(String phone) {
        this.phone = phone;
    }

    public UUID getIdPhone() {
        return idPhone;
    }

    public void setIdPhone(UUID idPhone) {
        this.idPhone = idPhone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<UserPhone> getUserPhones() {
        return userPhones;
    }

    public void setUserPhones(List<UserPhone> userPhones) {
        this.userPhones = userPhones;
    }
}