package com.example.crud_test_demo2.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column
    private String surname;
    @Column
    private Boolean isWorking;

    public User(){}

    public User(Integer id, String name, String surname, Boolean isWorking) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.isWorking = isWorking;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Boolean getWorking() {
        return isWorking;
    }

    public void setWorking(Boolean working) {
        isWorking = working;
    }
}
