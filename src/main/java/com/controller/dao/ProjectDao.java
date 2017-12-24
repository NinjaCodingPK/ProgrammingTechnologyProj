/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.dao;

import com.wookie.devteam.entities.Project;
import java.sql.Connection;
import java.util.Set;

public interface ProjectDao extends GenericDao<Project> {
    Project findByName(Connection connection, Project p);
    
    /**
     * Method finds Projects with "processed" status.
     * @param connection sql connection.
     * @return 
     */
    Set<Project> findProjectsToProcess(Connection connection);
    
    Set<Project> findByPerson(Connection connection, int personId);
    
    
//    Set<Project> findByPersonPagination(Connection connection, int personId);
}
