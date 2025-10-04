package com.mediapp.juanb.juanm.mediapp.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_city", updatable = false, nullable = false, unique = true)
    private UUID idCity;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "city")
    private List<User> users = new ArrayList<>();

    public City() {}

    public City(String name) {
        this.name = name;
    }

    public UUID getIdCity() {
        return idCity;
    }

    public void setIdCity(UUID idCity) {
        this.idCity = idCity;
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