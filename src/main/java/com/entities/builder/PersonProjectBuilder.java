/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wookie.devteam.entities.builder;

import com.wookie.devteam.entities.Person;
import com.wookie.devteam.entities.PersonProject;
import com.wookie.devteam.entities.Project;
import java.util.Date;

/**
 *
 * @author wookie
 */
public class PersonProjectBuilder {
    private PersonProject instance;
    
    public PersonProjectBuilder() {
        instance = new PersonProject();
    }

    public PersonProjectBuilder setPerson(Person person) {
        instance.setPerson(person);
        return this;
    }

    public PersonProjectBuilder setProject(Project project) {
        instance.setProject(project);
        return this;
    }

    public PersonProjectBuilder setTime(int time) {
        instance.setTime(time);
        return this;
    }
    
    public PersonProject build() {
        return instance;
    }
}
