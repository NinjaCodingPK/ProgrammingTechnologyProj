/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.dao.jdbc;

import com.wookie.devteam.dao.DaoFactory;
import com.wookie.devteam.dao.PersonDao;
import com.wookie.devteam.dao.PersonProjectDao;
import com.wookie.devteam.dao.ProjectDao;
import com.wookie.devteam.dao.ProjectQualificationDao;
import com.wookie.devteam.dao.QualificationDao;
import com.wookie.devteam.dao.RoleDao;
import com.wookie.devteam.dao.StatusDao;
import com.wookie.devteam.dao.TaskDao;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;

public class JdbcDaoFactory extends DaoFactory {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(JdbcDaoFactory.class);
    private final static String POOL_NAME = "jdbc/DevTeamResource";
    private static DataSource ds;

    public JdbcDaoFactory() {
        try {
            InitialContext ic = new InitialContext();
            ds = (DataSource) ic.lookup(POOL_NAME);
        } catch (NamingException e) {
            logger.error("Error while processing database " + e);
        }
    }

    public Connection getConnection() {
        try {
            logger.info("Get connection");
            return ds.getConnection();
        } catch (SQLException ex) {
            logger.error("Exception while getting connection " + ex);
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public DataSource getDataSource() {
        return ds;
    }

    @Override
    public PersonDao createPersonDao() {
        return new JdbcPersonDao();
    }

    @Override
    public PersonProjectDao createPersonProjectDao() {
        return new JdbcPersonProjectDao();
    }

    @Override
    public ProjectDao createProjectDao() {
        return new JdbcProjectDao();
    }

    @Override
    public QualificationDao createQualificationDao() {
        return new JdbcQualificationDao();
    }

    @Override
    public RoleDao createRoleDao() {
        return new JdbcRoleDao();
    }
    
    @Override
    public TaskDao createTaskDao() {
        return new JdbcTaskDao();
    }

    @Override
    public ProjectQualificationDao createProjectQualificationDao() {
        return new JdbcProjectQualificationDao();
    }

    @Override
    public StatusDao createStatusDao() {
        return new JdbcStatusDao();
    }
    
    
    
}
