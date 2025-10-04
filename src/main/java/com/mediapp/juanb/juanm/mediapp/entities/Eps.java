package com.mediapp.juanb.juanm.mediapp.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "eps")
public class Eps {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_eps", updatable = false, nullable = false, unique = true)
    private UUID idEps;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "eps")
    private List<User> users = new ArrayList<>();

    public Eps() {}

    public Eps(String name) {
        this.name = name;
    }

    public UUID getIdEps() {
        return idEps;
    }

    public void setIdEps(UUID idEps) {
        this.idEps = idEps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}