package com.mediapp.juanb.juanm.mediapp.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mediapp.juanb.juanm.mediapp.entities.User;
import com.mediapp.juanb.juanm.mediapp.services.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/usuarios")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll(){
        List<User> list = userService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{cedula}")
    public Optional<User> getByCedula(@PathVariable("cedula") String cedula){
        return userService.findByCedula(cedula);
    }
    
    @PostMapping
    public ResponseEntity<User> save(@RequestBody User user) {
        User newUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PutMapping("/{cedula}")
    public ResponseEntity<User> update(@PathVariable("cedula") String cedula, @RequestBody User user) {
        User updatedUser = userService.update(cedula, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{cedula}")
    public ResponseEntity<Void> delete(@PathVariable("cedula") String cedula) {
        userService.delete(cedula);
        return ResponseEntity.noContent().build();
    }   
}
