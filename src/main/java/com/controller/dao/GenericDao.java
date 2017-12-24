/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.dao;

import java.sql.Connection;
import java.util.Set;

/**
 * Interface which handles similar operations for all DAO classes.
 *
 */
interface GenericDao<E> {
    E create(Connection connection, E e);
    boolean update(Connection connection, E e);
    boolean delete(Connection connection, E e);
    E findById(Connection connection, int... id);
    Set<E> findAll(Connection connection);
}
