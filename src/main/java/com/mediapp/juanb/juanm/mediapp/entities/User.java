package com.mediapp.juanb.juanm.mediapp.entities;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @Column(name = "cedula", nullable = false, unique = true)
    private String cedula;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "city")
    private City city;

    @ManyToOne
    @JoinColumn(name = "eps")
    private Eps eps;

    @OneToMany(mappedBy = "user")
    private List<UserPhone> userPhones = new ArrayList<>();

    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments = new ArrayList<>();

    public User() {}

    public User(String cedula, String name, String email, String password, City city, Eps eps) {
        this.cedula = cedula;
        this.name = name;
        this.email = email;
        this.password = password;
        this.city = city;
        this.eps = eps;
    }

    public String getCedula() { 
        return cedula; 
    }

    public void setCedula(String cedula) { 
        this.cedula = cedula; 
    }

    public String getName() { 
        return name; 
    }

    public void setName(String name) { 
        this.name = name; 
    }

    public String getEmail() { 
        return email; 
    }

    public void setEmail(String email) { 
        this.email = email; 
    }

    public void setPassword(String password) { 
        this.password = password; 
    }

    public City getCity() { 
        return city; 
    }

    public void setCity(City city) { 
        this.city = city; 
    }

    public Eps getEps() { 
        return eps; 
    }

    public void setEps(Eps eps) { 
        this.eps = eps;
    }

    public List<UserPhone> getUserPhones() { 
        return userPhones; 
    }

    public void setUserPhones(List<UserPhone> userPhones) { 
        this.userPhones = userPhones; 
    }

    public List<Appointment> getAppointments() { 
        return appointments; 
    }

    public void setAppointments(List<Appointment> appointments) { 
        this.appointments = appointments; 
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.cedula;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}