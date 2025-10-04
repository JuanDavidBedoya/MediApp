package com.mediapp.juanb.juanm.mediapp.entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user_phones")
public class UserPhone {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_user_phone", updatable = false, nullable = false, unique = true)
    private UUID idUserPhone;

    @ManyToOne
    @JoinColumn(name = "cedula_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_phone")
    private Phone phone;

    public UserPhone() {}

    public UserPhone(User user, Phone phone) {
        this.user = user;
        this.phone = phone;
    }

    public UUID getIdUserPhone() {
        return idUserPhone;
    }

    public void setIdUserPhone(UUID idUserPhone) {
        this.idUserPhone = idUserPhone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }
}