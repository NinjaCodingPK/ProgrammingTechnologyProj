/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entities.builder;

import com.entities.Qualification;


public class QualificationBuilder {
    private Qualification instance;

    public QualificationBuilder() {
        instance = new Qualification();
    }
    
    public QualificationBuilder setId(int id) {
        instance.setId(id);
        return this;
    }

    public QualificationBuilder setName(String name) {
        instance.setName(name);
        return this;
    }
    
    public Qualification build() {
        return instance;
    }
}
