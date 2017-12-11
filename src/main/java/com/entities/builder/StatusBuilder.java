/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entities.builder;

import com.entities.Status;


public class StatusBuilder {
    private Status instance;

    public StatusBuilder() {
        instance = new Status();
    }

    public StatusBuilder setId(int id) {
        instance.setId(id);
        return this;
    }

    public StatusBuilder setName(String name) {
        instance.setName(name);
        return this;
    }
    
    public Status build() {
        return instance;
    }
}
