/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.dao;

import com.wookie.devteam.entities.PersonProject;
import java.sql.Connection;
import java.util.Set;

public interface PersonProjectDao extends GenericDao<PersonProject> {
    Set<PersonProject> findByProject(Connection connection, int id);
    Set<PersonProject> findByPerson(Connection connection, int id);
}
