/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.dao;

import com.wookie.devteam.entities.ProjectQualification;
import java.sql.Connection;
import java.util.Set;

public interface ProjectQualificationDao extends GenericDao<ProjectQualification> {
    Set<ProjectQualification> findByProject(Connection connection, int projectId); 
    Set<ProjectQualification> findByQualification(Connection connection, int qualificationId); 
}
