package com.mediapp.juanb.juanm.mediapp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "recepcionistas")
public class Receptionist extends User{
    @OneToOne(mappedBy = "receptionist")
    @JoinColumn(name = "cedula_recepcionista")
    private User user;

    public Receptionist() {
        super();
    }

    public Receptionist(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
