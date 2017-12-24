 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.dao;

import com.wookie.devteam.entities.Status;
import java.sql.Connection;

public interface StatusDao extends GenericDao<Status> {
    Status findByName(Connection connection, String name); 
}
