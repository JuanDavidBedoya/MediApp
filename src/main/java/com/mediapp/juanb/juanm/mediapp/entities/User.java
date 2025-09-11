package com.mediapp.juanb.juanm.mediapp.entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class User {

    @Id
    private String cedula;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "gender")
    private String gender;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "id_role", referencedColumnName = "idRole", nullable = false)
    private Role role;

    @ManyToMany
    @JoinTable(
    name = "doctor_especialidad",
    joinColumns = @JoinColumn(name = "cedula_doctor", referencedColumnName = "cedula"),
    inverseJoinColumns = @JoinColumn(name = "id_especialidad", referencedColumnName = "uuidSpeciality")
)
private List<Speciality> specialties;

    public User() {}

    public User(String cedula, String firstName, String lastName, LocalDate birthDate, String gender, String phone,
            String email, String address, String password, Role role) {
        this.cedula = cedula;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.password = password;
        this.role = role;
    }

    public String getCedula() { 
        return cedula; 
    }

    public void setCedula(String cedula) { 
        this.cedula = cedula; 
    }

    public String getFirstName() { 
        return firstName; 
    }
    public void setFirstName(String firstName) { 
        this.firstName = firstName; 
    }

    public String getLastName() { 
        return lastName; 
    }
    public void setLastName(String lastName) { 
        this.lastName = lastName; 
    }

    public LocalDate getBirthDate() {
        return birthDate; 
    }
    public void setBirthDate(LocalDate birthDate) { 
        this.birthDate = birthDate; 
    }

    public String getGender() { 
        return gender; 
    }
    public void setGender(String gender) { 
        this.gender = gender; 
    }

    public String getPhone() { 
        return phone; 
    }
    public void setPhone(String phone) { 
        this.phone = phone; 
    }

    public String getEmail() { 
        return email; 
    }
    public void setEmail(String email) { 
        this.email = email; 
    }

    public String getAddress() { 
        return address; 
    }
    public void setAddress(String address) { 
        this.address = address; 
    }

    public String getPassword() { 
        return password; 
    }
    public void setPassword(String password) { 
        this.password = password; 
    }

    public Role getRole() {
        return role; 
    }
    public void setRole(Role role) { 
        this.role = role; 
    }
}
