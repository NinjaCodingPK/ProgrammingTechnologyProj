/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entities.builder;

import com.entities.Person;
import com.entities.Qualification;
import com.entities.Role;
import java.sql.Date;


public class PersonBuilder {
    private Person instance;
    
    public PersonBuilder() {
        instance = new Person();
    }

    public PersonBuilder setId(int id) {
        instance.setId(id);
        return this;
    }

    public PersonBuilder setName(String name) {
        instance.setName(name);
        return this;
    }

    public PersonBuilder setSurname(String surname) {
        instance.setSurname(surname);
        return this;
    }

    public PersonBuilder setPhone(String phone) {
        instance.setPhone(phone);
        return this;
    }

    public PersonBuilder setBirthday(Date date) {
        instance.setBirthday(date);
        return this;
    }

    public PersonBuilder setStatus(boolean status) {
        instance.setStatus(status);
        return this;
    }

    public PersonBuilder setRole(Role role) {
        instance.setRole(role);
        return this;
    }
    
    public PersonBuilder setQualification(Qualification qualification) {
        instance.setQualification(qualification);
        return this;
    }

    public PersonBuilder setLogin(String login) {
        instance.setLogin(login);
        return this;
    }

    public PersonBuilder setPassword(String password) {
        instance.setPassword(password);
        return this;
    }
    
    public PersonBuilder setEmail(String email) {
        instance.setEmail(email);
        return this;
    }
    
    public Person build() {
        return instance;
    }
}
