package com.mediapp.juanb.juanm.mediapp.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ususarios")
public class User {
    
    @Id
    private String cedula;
    @Column(name = "nombre")
    private String firstName;
    @Column(name = "apellido")
    private String lastName;
    @Column(name = "fecha_nacimiento")
    private Date birthDate;
    @Column(name = "genero")
    private String gender;
    @Column(name = "telefono")
    private String phone;
    @Column(name = "email")
    private String email;
    @Column(name = "direccion")
    private String address;
    @Column(name = "contrasena")
    private String password;
    @Column(name = "rol")
    private String role;
    
    
    public User() {
    }

    public User(String cedula, String firstName, String lastName, Date birthDate, String gender, String phone,
            String email, String address, String password, String role) {
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}