/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wookie.devteam.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author wookie
 */
public class Project {
    private Integer id;
    private String name;
    private Date deadline;
    private Status status;
    private BigDecimal cost;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getDeadline() {
        return deadline;
    }

    public Status getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Project{" + "id=" + id + ", name=" + name + ", deadline=" +
                deadline + ", status=" + status + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.id);
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
        final Project other = (Project) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    
}
