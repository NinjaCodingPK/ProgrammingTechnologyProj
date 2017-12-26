a/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import java.sql.Connection;
import javax.sql.DataSource;

/**
 * Abstract factory for easier using of DAO classes and database connection.
 */
public abstract class DaoFactory {
    public final static String FACTORY_PASS = "com.wookie.devteam.dao.jdbc.JdbcDaoFactory";
    
    private static DaoFactory factory;
    
    public abstract PersonDao createPersonDao();
    public abstract PersonProjectDao createPersonProjectDao();+
    public abstract ProjectDao createProjectDao();
    public abstract QualificationDao createQualificationDao();
    public abstract ProjectQualificationDao createProjectQualificationDao();
    public abstract RoleDao createRoleDao();
    public abstract TaskDao createTaskDao();
    public abstract StatusDao createStatusDao();

    public abstract DataSource getDataSource();
    public abstract Connection getConnection();
    
    public static DaoFactory getFactory() {
        try {
            if(factory == null)
                factory = (DaoFactory) Class.forName(FACTORY_PASS).newInstance();
            
            return factory;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
