/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entities.builder;

import com.entities.Person;
import com.entities.PersonProject;
import com.entities.Project;
import java.util.Date;


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
