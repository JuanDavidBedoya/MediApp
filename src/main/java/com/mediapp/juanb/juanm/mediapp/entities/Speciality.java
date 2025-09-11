package com.mediapp.juanb.juanm.mediapp.entities;

import java.util.List;
import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "especialidades")
public class Speciality {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuidSpeciality;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "specialties")
    private List<User> doctors;

    public Speciality() {
    }

    public Speciality(UUID uuidSpeciality, String name) {
        this.uuidSpeciality = uuidSpeciality;
        this.name = name;
    }

    public UUID getUuid() {
        return uuidSpeciality;
    }

    public void setId(UUID uuidSpeciality) {
        this.uuidSpeciality = uuidSpeciality;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}