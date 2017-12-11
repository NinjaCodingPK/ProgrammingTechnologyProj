/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entities.builder;

import com.entities.Project;
import com.entities.Status;
import com.entities.Task;


public class TaskBuilder {
    private Task instance;
    
    public TaskBuilder() {
        instance = new Task();
    }

    public TaskBuilder setId(int id) {
        instance.setId(id);
        return this;
    }

    public TaskBuilder setText(String text) {
        instance.setText(text);
        return this;
    }

    public TaskBuilder setProject(Project project) {
        instance.setProject(project);
        return this;
    }

    public TaskBuilder setStatus(Status status) {
        instance.setStatus(status);
        return this;
    }
    
    public Task build() {
        return instance;
    }
}
