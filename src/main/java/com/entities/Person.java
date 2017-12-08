/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wookie.devteam.entities;

import java.sql.Date;
import java.util.Objects;


/**
 *
 * @author wookie
 */
public class Person {
    private Integer id;
    private String name;
    private String surname;
    private String phone;
    private Date birthday;
    private Boolean status;
    private Role role;
    private Qualification qualification;
    private String login;
    private String password;
    private String email;

    /**
     * Calculates and age of a person using it's birth date.
     * @return age of a person.
     */
    public long getAge() {
        final int dateKoef = 90015036;
        final int daysInYear = 365;
                
        long age = Math.abs(new java.util.Date().getTime() - birthday.getTime());
        
        return age / daysInYear / dateKoef;
    }
    
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhone() {
        return phone;
    }

    public Date getBirthday() {
        return birthday;
    }

    public Boolean getStatus() {
        return status;
    }

    public Role getRole() {
        return role;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Qualification getQualification() {
        return qualification;
    }

    public void setQualification(Qualification qualification) {
        this.qualification = qualification;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", name=" + name + ", surname=" + surname 
                + ", phone=" + phone + ", birthday=" + birthday + ", status=" 
                + status + ", role=" + role + ", qualification=" + qualification 
                + ", login=" + login + ", password=" + password + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Person other = (Person) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }


    
}
