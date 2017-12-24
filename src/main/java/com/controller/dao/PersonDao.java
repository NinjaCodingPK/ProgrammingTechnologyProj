/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.dao;

import com.wookie.devteam.entities.Person;
import java.sql.Connection;
import java.util.Set;

public interface PersonDao extends GenericDao<Person> {
    Person findByLogin(Connection connection, String login);
    
    /**
     * Method finds all developers with "free" status.
     * @param connection sql connection.
     * @return Set of Person entities.
     */
    Set<Person> findFreeDevs(Connection connection);
}
