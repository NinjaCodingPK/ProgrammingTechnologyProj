/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entities.builder;

import com.entities.Project;
import com.entities.Status;

import java.math.BigDecimal;
import java.util.Date;


public class ProjectBuilder {
    private Project instance;
    
    public ProjectBuilder() {
        instance = new Project();
    }

    public ProjectBuilder setId(int id) {
        instance.setId(id);
        return this;
    }

    public ProjectBuilder setName(String name) {
        instance.setName(name);
        return this;
    }

    public ProjectBuilder setDeadline(Date deadline) {
        instance.setDeadline(deadline);
        return this;
    }

    public ProjectBuilder setStatus(Status status) {
        instance.setStatus(status);
        return this;
    }
    
    public ProjectBuilder setCost(BigDecimal cost) {
        instance.setCost(cost);
        return this;
    }
    
    public Project build() {
        return instance;
    }
}
