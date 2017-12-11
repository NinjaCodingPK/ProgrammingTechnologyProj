/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entities.builder;

import com.entities.Role;


public class RoleBuilder {
    private Role instance;
    
    public RoleBuilder() {
        instance = new Role();
    }

    public RoleBuilder setId(int id) {
        instance.setId(id);
        return this;
    }

    public RoleBuilder setName(String name) {
        instance.setName(name);
        return this;
    }
    
    public Role build() {
        return instance;
    }
}
