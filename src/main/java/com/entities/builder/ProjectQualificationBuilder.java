/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wookie.devteam.entities.builder;

import com.wookie.devteam.entities.Project;
import com.wookie.devteam.entities.ProjectQualification;
import com.wookie.devteam.entities.Qualification;

/**
 *
 * @author wookie
 */
public class ProjectQualificationBuilder {
    private ProjectQualification instance;

    public ProjectQualificationBuilder() {
        instance = new ProjectQualification();
    }
    
    public ProjectQualificationBuilder(ProjectQualification instance) {
        this.instance = instance;
    }  

    public ProjectQualificationBuilder setProject(Project project) {
        instance.setProject(project);
        return this;
    }

    public ProjectQualificationBuilder setQualification(Qualification qualification) {
        instance.setQualification(qualification);
        return this;
    }
    
    public ProjectQualificationBuilder setCount(int count) {
        instance.setCount(count);
        return this;
    }
    
    public ProjectQualification build() {
        return instance;
    }
}
