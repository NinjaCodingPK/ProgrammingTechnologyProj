/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.dao.jdbc;

import com.wookie.devteam.dao.PersonProjectDao;
import com.wookie.devteam.entities.PersonProject;
import com.wookie.devteam.entities.builder.PersonBuilder;
import com.wookie.devteam.entities.builder.PersonProjectBuilder;
import com.wookie.devteam.entities.builder.ProjectBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import org.apache.logging.log4j.LogManager;

public class JdbcPersonProjectDao implements PersonProjectDao {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(JdbcPersonProjectDao.class);
    
    public static final String TABLE_NAME = "PersonProject";
    public static final String COLUMN_PERSON = TABLE_NAME + ".person_id";
    public static final String COLUMN_PROJECT = TABLE_NAME + ".project_id";
    public static final String COLUMN_TIME = TABLE_NAME + ".time";
    
    public static final String CREATE_STATEMENT =
            "INSERT INTO " + TABLE_NAME + " (person_id, project_id)"
            + " VALUES (?, ?);";
    public static final String UPDATE_STATEMENT =
            "UPDATE " + TABLE_NAME + " SET time=? WHERE person_id=? AND project_id=?;";
    public static final String DELETE_STATEMENT =
            "DELETE FROM " + TABLE_NAME + " WHERE person_id = ? AND project_id=?;";
    public static final String FIND_BY_ID_STATEMENT =
            "SELECT * from " + TABLE_NAME + " WHERE person_id = ? AND project_id=?;";
    public static final String FIND_BY_PROJECT_STATEMENT =
            "SELECT * from " + TABLE_NAME + " WHERE project_id=?;";
    public static final String FIND_BY_PERSON_STATEMENT =
            "SELECT * from " + TABLE_NAME + " WHERE person_id=?;";
    public static final String FIND_ALL_STATEMENT =
            "SELECT * from " + TABLE_NAME + ";";
    
    private PersonProject getResult(ResultSet rs) throws SQLException {
        return new PersonProjectBuilder()
                    .setPerson(new PersonBuilder().setId(rs.getInt(COLUMN_PERSON)).build())
                    .setProject(new ProjectBuilder().setId(rs.getInt(COLUMN_PROJECT)).build())
                    .setTime(rs.getInt(COLUMN_TIME))
                    .build();
    }
    
    @Override
    public PersonProject create(Connection connection, PersonProject e) {
        try (PreparedStatement preparedStatement = 
                    connection.prepareStatement(CREATE_STATEMENT))
        {
            preparedStatement.setInt(1, e.getPerson().getId());
            preparedStatement.setInt(2, e.getProject().getId());
            preparedStatement.executeUpdate();
            
            preparedStatement.close();
            return e;
        } catch (SQLException ex) {
            logger.error("Error while database processing " + ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean update(Connection connection, PersonProject e) {
        try (PreparedStatement preparedStatement = 
                connection.prepareStatement(UPDATE_STATEMENT))
        {
            preparedStatement.setInt(1, e.getTime());
            preparedStatement.setInt(2, e.getPerson().getId());
            preparedStatement.setInt(3, e.getProject().getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            
            return true;
        } catch (SQLException ex) {
            logger.error("Error while database processing " + ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean delete(Connection connection, PersonProject e) {
        try (PreparedStatement preparedStatement = 
                    connection.prepareStatement(DELETE_STATEMENT))
        {
            preparedStatement.setInt(1, e.getPerson().getId());
            preparedStatement.setInt(2, e.getProject().getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            
            return true;
        } catch (SQLException ex) {
            logger.error("Error while database processing " + ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public PersonProject findById(Connection connection, int... id) {
        try (PreparedStatement preparedStatement = 
                    connection.prepareStatement(FIND_BY_ID_STATEMENT))
        {
            preparedStatement.setInt(1, id[0]);
            preparedStatement.setInt(2, id[1]);
            ResultSet rs = preparedStatement.executeQuery();

            PersonProject temp = null;
            if(rs.next()) {
                temp = getResult(rs);
            }
        
            preparedStatement.close();
            return temp;
        } catch (SQLException ex) {
            logger.error("Error while database processing " + ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Set<PersonProject> findAll(Connection connection) {
        try (Statement query = connection.createStatement())
        {
            ResultSet rs = query.executeQuery(FIND_ALL_STATEMENT);
            Set<PersonProject> res = new HashSet<>();
            while (rs.next()) {
                res.add(getResult(rs));
            }
            
            query.close();
            return res;
        } catch (SQLException ex) {
            logger.error("Error while database processing " + ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Set<PersonProject> findByProject(Connection connection, int id) { 
        try (PreparedStatement preparedStatement =
                    connection.prepareStatement(FIND_BY_PROJECT_STATEMENT))
        {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            Set<PersonProject> res = new HashSet<>();
            while (rs.next()) {
                res.add(getResult(rs));
            }
        
            preparedStatement.close();
            return res;
        } catch (SQLException ex) {
            logger.error("Error while database processing " + ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Set<PersonProject> findByPerson(Connection connection, int id) {
        try (PreparedStatement preparedStatement = 
                    connection.prepareStatement(FIND_BY_PERSON_STATEMENT))
        {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            Set<PersonProject> res = new HashSet<>();
            while (rs.next()) {
                res.add(getResult(rs));
            }
        
            preparedStatement.close();
            return res;
        } catch (SQLException ex) {
            logger.error("Error while database processing " + ex);
            throw new RuntimeException(ex);
        }
    }
    
}
